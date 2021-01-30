package com.kai.seckill.controller;


import com.kai.seckill.pojo.User;
import com.kai.seckill.service.IGoodsService;
import com.kai.seckill.service.IUserService;
import com.kai.seckill.vo.DetailVo;
import com.kai.seckill.vo.GoodsVo;
import com.kai.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;

    // 引入redis
    @Autowired
    private RedisTemplate redisTemplate;
    // 引入模板引擎
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

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

//    // 跳转到商品列表页,通过MVC做了参数配置
//    // @RequestMapping("/toList")
//    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toList(Model model, User user) {
//
//        System.out.println("toList==>"+user);
//        if (null == user) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//        model.addAttribute("goodsList", goodsService.findGoodsVo());
//        return "goodsList";
//    }

    // 跳转到商品列表页,通过MVC做了参数配置,并进行页面缓存
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        // 获取页面
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
//        System.out.println("toList==>"+user);
//        if (null == user) {
//            return "login";
//        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());

        // 如果页面为空,手动渲染,并存入Redis
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

//    @RequestMapping("/toDetail/{goodsId}")
//    public String toDetail(Model model, User user, @PathVariable("goodsId") Long goodsId) {
//        model.addAttribute("user", user);
//        GoodsVo goodsVoByGoodsId = goodsService.findGoodsVoByGoodsId(goodsId);
//        int [] status=goodsService.getStatus(goodsId);
//        int secKillStatus=status[0];
//        int remainSeconds=status[1];
//
//        model.addAttribute("secKillStatus", secKillStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//        model.addAttribute("goods", goodsVoByGoodsId);
//        return "goodsDetail";
//    }
    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable("goodsId") Long goodsId,HttpServletRequest request,HttpServletResponse response) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail" + goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVoByGoodsId = goodsService.findGoodsVoByGoodsId(goodsId);
        int[] status = goodsService.getStatus(goodsId);
        int secKillStatus = status[0];
        int remainSeconds = status[1];

        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodsVoByGoodsId);
        //return "goodsDetail";
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        return html;
    }

    // 页面静态化
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable("goodsId") Long goodsId) {

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        int[] status = goodsService.getStatus(goodsId);
        int secKillStatus = status[0];
        int remainSeconds = status[1];

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
