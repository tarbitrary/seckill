package net.xicp.tarbitrary.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author tarbitrary
 */
@Data
public class SeckillGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
