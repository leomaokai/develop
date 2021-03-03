package com.kai.controller;


import com.kai.pojo.User;
import com.kai.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public String login(User user, HttpSession session){
        if(user==null){
            return "redirect:/login";
        }
        User res = userService.login(user);
        if(res!=null){
            session.setAttribute("user",res);
            return "redirect:/file/showAll";
        }else {
            return "redirect:/login";
        }
    }
}
