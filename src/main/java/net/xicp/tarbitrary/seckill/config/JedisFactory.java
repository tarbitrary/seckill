package net.xicp.tarbitrary.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisFactory {


    @Bean
    public JedisPool jedisPool(JedisConfig jedisConfig) {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfig.getMaxIdle());
        jedisPoolConfig.setMaxTotal(jedisConfig.getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(jedisConfig.getMaxWaitMills() * 1000);

        final JedisPool jedisPool = new JedisPool(jedisPoolConfig, jedisConfig.getHost(), jedisConfig.getPort(), jedisConfig.getTimeout() * 1000, jedisConfig.getPassword(), 0);

        return jedisPool;
    }


}
