package net.xicp.tarbitrary.seckill.vo;

import lombok.Data;
import net.xicp.tarbitrary.seckill.domain.Goods;

import java.util.Date;

@Data
public class GoodsVO extends Goods {

    private Long seckillGoodsId;
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    private long startTime;

    private long endTime;
}
