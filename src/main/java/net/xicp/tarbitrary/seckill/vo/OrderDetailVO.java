package net.xicp.tarbitrary.seckill.vo;

import lombok.Data;
import net.xicp.tarbitrary.seckill.domain.Goods;
import net.xicp.tarbitrary.seckill.domain.OrderInfo;

@Data
public class OrderDetailVO {
    private OrderInfo order;

    private Goods goods;

}
