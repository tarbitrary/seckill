package net.xicp.tarbitrary.seckill.mq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class Receiver extends MessageListenerAdapter {

    void handleMessage(String message) {
        log.info("receive message: {}", message);
    }
}
