package net.xicp.tarbitrary.seckill.mq;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.config.RabbitMQConfig;
import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
import net.xicp.tarbitrary.seckill.util.BeanUtil;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecKillMessageReceiver {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitListener(queues = RabbitMQConfig.SEC_KILL_QUEUE_NAME)
    public void secKillReceive(String message) {
        log.info("receive message:" + message);
        final SecKillMessage secKillMessage = BeanUtil.string2Bean(message, SecKillMessage.class);

        final GoodsVO goodsById = goodsService.getGoodsById(secKillMessage.getGoodsId());
        if (null == goodsById) {
            return;
        }

        if (goodsById.getGoodsStock() <= 0) {
            return;
        }

        final SeckillOrder seckillOrder = seckillOrderService.querySeckillOrderByUserIdAndGoodsId(secKillMessage.getTradeUser().getId(), secKillMessage.getGoodsId());

        if (null != seckillOrder) {
            return;
        }

        final OrderInfo orderInfo = seckillOrderService.doSeckill(secKillMessage.getTradeUser(), goodsById);


    }
}


