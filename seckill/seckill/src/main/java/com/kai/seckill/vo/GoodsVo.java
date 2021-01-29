package com.kai.seckill.vo;


import com.kai.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

//商品返回对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {

    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}
