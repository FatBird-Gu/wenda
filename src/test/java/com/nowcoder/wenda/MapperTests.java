package com.nowcoder.wenda;

import com.nowcoder.wenda.dao.DiscussPostMapper;
import com.nowcoder.wenda.dao.LoginTicketMapper;
import com.nowcoder.wenda.dao.MessageMapper;
import com.nowcoder.wenda.dao.UserMapper;
import com.nowcoder.wenda.entity.DiscussPost;
import com.nowcoder.wenda.entity.LoginTicket;
import com.nowcoder.wenda.entity.Message;
import com.nowcoder.wenda.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@SpringBootTest

public class MapperTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void testSelect(){
//        User user = userMapper.selectById(101);
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInserUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("ava");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser(){
        int rows = userMapper.updateStatus(150,1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150,"hello");
        System.out.println(rows);
    }
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(149, 0,10);
        for (DiscussPost post:
             discussPostList) {
            System.out.println(post);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testInsertLogin(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000 * 60 * 10));
        loginTicketMapper.insertLogininTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",1);
    }

    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testSelectLetters(){
        List<Message> list = messageMapper.selectConversations(111,0,20);
        for (Message msg : list){
            System.out.println(msg);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_131",0,10);
        for (Message msg : list){
            System.out.println(msg);
        }
        count = messageMapper.selectLetterCount("111_131");
        System.out.println(count);
    }
}
