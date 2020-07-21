package net.xicp.tarbitrary.seckill.redis;

import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.KeyPrefix;
import net.xicp.tarbitrary.seckill.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author tarbitrary
 */
@Service
public class RedisCacheServiceImpl implements CacheService {
    @Autowired
    private JedisPool jedisPool;


    @Override
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        if (value == null) {
            return false;
        }

        Jedis resource = null;

        try {
            final Class<?> aClass =
                    value.getClass();
            resource = jedisPool.getResource();
            String realKey = prefix.buildKey(key);
            String realVal = BeanUtil.bean2String(value);

            if (null == realVal || realVal.length() <= 0) {
                return false;
            }

            if (prefix.expireSeconds() > 0) {
                resource.setex(realKey, prefix.expireSeconds(), realVal);
            } else {
                resource.set(realKey, realVal);
            }

            return true;


        } catch (Exception e) {
            return false;

        } finally {
            returnToPool(resource);
        }
    }

    private void returnToPool(Jedis resource) {
        if (null != resource) {
            resource.close();
        }
    }

    @Override
    public <T> T get(KeyPrefix prefix, String key, Class<T> t) {
        Jedis resource = null;

        try {
            resource = jedisPool.getResource();
            final String realKey =
                    prefix.buildKey(key);
            final String sResult = resource.get(realKey);
            T result = BeanUtil.string2Bean(sResult, t);
            return result;

        } finally {
            returnToPool(resource);
        }
    }



    @Override
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            final String realKey = prefix.buildKey(key);

            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean delete(KeyPrefix prefix, String key) {
        final String realKey = prefix.buildKey(key);
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            final Long del = jedis.del(realKey);
            return del > 0L;
        } finally {
            returnToPool(jedis);
        }
    }



    @Override
    public Long incr(KeyPrefix prefix, String key) {
        Jedis resource = null;

        try {
            resource = jedisPool.getResource();

            final String realKey = prefix.buildKey(key);
            final Long incr = resource.incr(realKey);
            return incr;

        } finally {
            returnToPool(resource);
        }

    }

    @Override
    public Long decr(KeyPrefix prefix, String key) {
        Jedis resource = null;

        try {
            resource = jedisPool.getResource();

            final String realKey = prefix.buildKey(key);
            final Long decr = resource.decr(realKey);
            return decr;

        } finally {
            returnToPool(resource);
        }

    }

}
