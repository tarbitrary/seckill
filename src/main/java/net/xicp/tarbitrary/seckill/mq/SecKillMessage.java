package net.xicp.tarbitrary.seckill.mq;

import lombok.Data;
import net.xicp.tarbitrary.seckill.domain.TradeUser;

@Data
public class SecKillMessage {
    private TradeUser tradeUser;

    private Long goodsId;
}
