package com.kai.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.seckill.pojo.Order;
import com.kai.seckill.pojo.User;
import com.kai.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-29
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
