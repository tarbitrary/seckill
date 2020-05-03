package net.xicp.tarbitrary.seckill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Component
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




}
