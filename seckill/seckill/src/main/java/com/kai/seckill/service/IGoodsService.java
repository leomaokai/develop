package com.kai.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.seckill.pojo.Goods;
import com.kai.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kai
 * @since 2021-01-29
 */
public interface IGoodsService extends IService<Goods> {

    // 获取商品列表
    List<GoodsVo> findGoodsVo();

    // 获取商品详情
    GoodsVo findGoodsVoByGoodsId(Long goodsId);

    int[] getStatus(Long goodsId);
}
