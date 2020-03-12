package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.LoginTicketMapper;
import com.nowcoder.wenda.dao.UserMapper;
import com.nowcoder.wenda.entity.LoginTicket;
import com.nowcoder.wenda.entity.User;
import com.nowcoder.wenda.util.MailClient;
import com.nowcoder.wenda.util.WendaConstant;
import com.nowcoder.wenda.util.WendaUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.jws.Oneway;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements WendaConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${wenda.path.domin}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
        return  userMapper.selectById(id);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("userNameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u!=null){
            map.put("usernameMsg","该账号已存在");
            return  map;
        }
        //邮箱验证
        u = userMapper.selectByEmail(user.getEmail());
        if (u!=null){
            map.put("emailMsg","该邮箱已被注册");
            return  map;
        }

        // 注册用户
        user.setSalt(WendaUtil.gennerateUUID().substring(0,5));
        user.setPassword(WendaUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(WendaUtil.gennerateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/wenda/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg","账号不存在");
            return map;
        }
        // 验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","账号未激活");
            return map;
        }
        //验证密码
        password = WendaUtil.md5(password+user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(WendaUtil.gennerateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * expiredSeconds));
        loginTicketMapper.insertLogininTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public  void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public Map<String, Object> forget(String email){
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.selectByEmail(email);
        if (user == null){
            map.put("usernameMsg","该用户不存在！");
            return map;
        }
        String forgetCode = WendaUtil.gennerateUUID().substring(0,6);
        Context context = new Context();
        context.setVariable("email",email);
        context.setVariable("code",forgetCode);
        String text = templateEngine.process("/mail/forget",context);
        mailClient.sendMail(email, "重置密码", text);
        map.put("code",forgetCode);
        return map;
    }

    public void changePassword(String email, String password){
        User user = userMapper.selectByEmail(email);
        if (user == null){
            return;
        }
        String encodePass = WendaUtil.md5(password+user.getSalt());
        userMapper.updatePassword(user.getId(),encodePass);
    }
}
