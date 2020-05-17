package net.xicp.tarbitrary.seckill.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (SeckillGoods)实体类
 *
 * @author tarbitrary
 * @since 2020-05-17 20:13:31
 */
@Data
public class SeckillGoods implements Serializable {
    private static final long serialVersionUID = -16143711155166500L;
    /**
     * 秒杀的商品表
     */
    private Long id;
    /**
     * 商品Id
     */
    private Long goodsId;
    /**
     * 秒杀价
     */
    private Double seckillPrice;
    /**
     * 库存数量
     */
    private Integer stockCount;
    /**
     * 秒杀开始时间
     */
    private Date startDate;
    /**
     * 秒杀结束时间
     */
    private Date endDate;


}