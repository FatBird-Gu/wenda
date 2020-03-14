package com.nowcoder.wenda.contoller;


import com.nowcoder.wenda.entity.DiscussPost;
import com.nowcoder.wenda.entity.User;
import com.nowcoder.wenda.service.DiscussPostService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.HostHolder;
import com.nowcoder.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if (user == null){
            return WendaUtil.getJSONString(403,"你还没有登录哦！");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        // 报错的情况 将来统一处理
        return WendaUtil.getJSONString(0,"发布成功！");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model){
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        // 帖子的作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        return "/site/discuss-detail";
    }
}
