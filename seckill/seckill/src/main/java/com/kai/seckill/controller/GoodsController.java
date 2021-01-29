package com.kai.seckill.controller;


import com.kai.seckill.pojo.User;
import com.kai.seckill.service.IGoodsService;
import com.kai.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;

//    // 跳转到商品列表页
//    @RequestMapping("/toList")
//    public String toList(Model model,@CookieValue("userTicket") String ticket,HttpServletRequest request,HttpServletResponse response) {
//
//        if (StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
//
//        // 通过session获得用户信息
//        // User user = (User) session.getAttribute(ticket);
//
//        //通过redis获取用户信息
//        User user = userService.getUserByCookie(ticket, request, response);
//        if (null == user) {
//             return "login";
//         }
//
//        System.out.println("1"+user);
//        model.addAttribute("user", user);
//        model.addAttribute("goodsList",goodsService.findGoodsVo());
//        return "goodsList";
//    }

    // 跳转到商品列表页,通过MVC做了参数配置
    @RequestMapping("/toList")
    public String toList(Model model, User user) {

        System.out.println(user);
        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        return "goodsList";
    }
}
