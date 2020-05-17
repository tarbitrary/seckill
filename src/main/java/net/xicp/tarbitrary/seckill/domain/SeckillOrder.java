package net.xicp.tarbitrary.seckill.domain;

import java.io.Serializable;

/**
 * (SeckillOrder)实体类
 *
 * @author tarbitrary
 * @since 2020-05-17 20:15:03
 */
public class SeckillOrder implements Serializable {
    private static final long serialVersionUID = -32657801259599983L;

    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 商品ID
     */
    private Long goodsId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

}