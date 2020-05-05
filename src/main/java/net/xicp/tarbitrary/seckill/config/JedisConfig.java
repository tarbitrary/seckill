package net.xicp.tarbitrary.seckill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
public class JedisConfig {
    private String host;

    private String password;

    private int port;

    private int maxIdle;

    private int total;

    private int maxTotal;
    private int timeout;
    private int maxWaitMills;

    @PostConstruct
    public void test() {
        System.out.println("hahaha");
    }


}
