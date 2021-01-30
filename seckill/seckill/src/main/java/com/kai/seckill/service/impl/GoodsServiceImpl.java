package com.kai.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kai.seckill.mapper.GoodsMapper;
import com.kai.seckill.pojo.Goods;
import com.kai.seckill.service.IGoodsService;
import com.kai.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kai
 * @since 2021-01-29
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private GoodsMapper goodsMapper;


    // 获取商品列表
    @Override
    public List<GoodsVo> findGoodsVo() {

        return goodsMapper.findGoodsVo();
    }

    // 获取商品详情
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }

    @Override
    public int[] getStatus(Long goodsId) {
        int[] status = new int[2];
        Date startDate = findGoodsVoByGoodsId(goodsId).getStartDate();
        Date endDate = findGoodsVoByGoodsId(goodsId).getEndDate();
        Date nowDate = new Date();
        status[0] = 0;
        status[1] = 0;
        // 秒杀还未开始
        if (nowDate.before(startDate)) {
            status[0] = 0;
            status[1] = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            // 秒杀结束
            status[0] = 2;
            status[1] = -1;
        } else {
            // 秒杀中
            status[0] = 1;
            status[1] = 0;
        }
        return status;
    }
}
