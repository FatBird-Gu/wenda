package com.nowcoder.wenda.contoller;

import com.nowcoder.wenda.entity.DiscussPost;
import com.nowcoder.wenda.entity.Page;
import com.nowcoder.wenda.entity.User;
import com.nowcoder.wenda.service.DiscussPostService;
import com.nowcoder.wenda.service.LikeService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.WendaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements WendaConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name = "orderMode",defaultValue = "0") int orderMode){
        // 方法调用之前 springmvc自动实例化Model与Page，并降Page自动注入Modal
        // 所以， thymeleaf中可以直接访问
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode="+orderMode);

        List<DiscussPost> list = discussPostService.findDiscussPost(0,page.getOffset(),page.getLimit(),orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list!=null){
            for (DiscussPost post:list){
                Map<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("orderMode",orderMode);
        return "/index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }

    // 拒绝访问时的提示页面
    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(){
        return "/error/404";
    }
}
