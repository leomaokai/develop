package com.kai.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kai.seckill.pojo.Order;
import com.kai.seckill.pojo.SeckillOrder;
import com.kai.seckill.pojo.User;
import com.kai.seckill.service.IGoodsService;
import com.kai.seckill.service.IOrderService;
import com.kai.seckill.service.ISeckillOrderService;
import com.kai.seckill.vo.GoodsVo;
import com.kai.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


@Controller
@RequestMapping("/secKill")
public class SecKillController {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private ISeckillOrderService seckillOrderService;

    @Resource
    private IOrderService orderService;


    @RequestMapping("/doSecKill")
    public String daSecKill(Model model, User user, Long goodsId){

        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        // 获得库存
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        // 判断是否重复抢购
        SeckillOrder one = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(one!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEAT_ERROR.getMessage());
            return "secKillFail";
        }
        // 抢购
        Order order=orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }

}
