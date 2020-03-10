package com.nowcoder.wenda.contoller;

import com.nowcoder.wenda.entity.User;
import com.nowcoder.wenda.service.AlphaService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;
    @RequestMapping("hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return  alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request,
                     HttpServletResponse response){
        // 请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+" : "+ value);

        }
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try(
                PrintWriter writer = response.getWriter(); // 可以不写final
                ) {
            writer.write("<h1>牛客网<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET 请求

    // /students?current=1&limit=20
    @RequestMapping(path = {"/students"}, method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false, defaultValue = "10") int limit){
        System.out.println(current+" "+limit);
        return "Some students";
    }

    // /student/{123}
    @RequestMapping(path = {"/student/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    // POST 请求
    // 也可以用@RequestParam(name = "current",required = false, defaultValue = "1") int current
    @RequestMapping(path = {"/student"}, method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println(name + " "+age);
        return "success";
    }

    // 响应html数据
    @RequestMapping(path = {"/teacher"}, method = RequestMethod.GET)
    public ModelAndView getTeacher(User user){
        System.out.println(user.toString());
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age","30");
        mav.setViewName("/demo/view"); //view.html
        return mav;
    }

    @RequestMapping(path = {"/school"}, method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","北大");
        model.addAttribute("age","1220");
        return "/demo/view";
    }

    // 响应JSON数据(异步)
    // JAVA对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = {"/emp"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000);
        return emp;
    }

    @RequestMapping(path = {"/emps"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps(){
        List<Map<String, Object>> list = new Vector<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","李四");
        emp.put("age",90);
        emp.put("salary",9000);

        list.add(emp);emp = new HashMap<>();
        emp.put("name","王三");
        emp.put("age",23);
        emp.put("salary",10000);
        list.add(emp);
        return list;
    }
}
