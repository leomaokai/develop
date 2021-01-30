package com.kai.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kai.seckill.mapper.OrderMapper;
import com.kai.seckill.pojo.Order;
import com.kai.seckill.pojo.SeckillGoods;
import com.kai.seckill.pojo.SeckillOrder;
import com.kai.seckill.pojo.User;
import com.kai.seckill.service.IOrderService;
import com.kai.seckill.service.ISeckillGoodsService;
import com.kai.seckill.service.ISeckillOrderService;
import com.kai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-29
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    // 抢购订单
    @Override
    public Order seckill(User user, GoodsVo goods) {

        // 秒杀商品表减库存
        SeckillGoods one = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        one.setStockCount(one.getStockCount()-1);
        seckillGoodsService.updateById(one);

        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(one.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        return order;
    }
}
