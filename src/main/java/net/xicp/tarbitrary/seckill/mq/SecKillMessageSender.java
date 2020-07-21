package net.xicp.tarbitrary.seckill.mq;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.config.RabbitMQConfig;
import net.xicp.tarbitrary.seckill.util.BeanUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecKillMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private CacheService cacheService;

    public void sendMessage(SecKillMessage message) {
        log.info("start to send message {}", message);
        final String info = BeanUtil.bean2String(message);
        amqpTemplate.convertAndSend(RabbitMQConfig.SEC_KILL_QUEUE_NAME, info);
        log.info("message send success {}", info);

    }
}
