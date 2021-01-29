package com.kai.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kai.seckill.pojo.Goods;
import com.kai.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kai
 * @since 2021-01-29
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    // 获取商品列表
    List<GoodsVo> findGoodsVo();
}
