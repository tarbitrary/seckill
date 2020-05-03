package net.xicp.tarbitrary.seckill.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tarbitrary
 */
@Service
public class RedisCacheServiceImpl implements CacheService {
    @Autowired
    private JedisPool jedisPool;

    private static final Set<Class> PRIMITIVE_TYPE_WRAPPED_SET = new HashSet<Class>() {
        {
            add(Byte.class);
            add(Short.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
            add(Character.class);
            add(Void.class);
        }
    };

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
            String realVal = bean2String(value);

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

    private <T> String bean2String(T value) {
        final Class<?> aClass = value.getClass();
        if (String.class == aClass) {
            return (String) value;
        }

        if (isPrimitiveWrapped(value)) {
            return "" + value;
        }

        return JSON.toJSONString(value);
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
            T result = string2Bean(sResult, t);
            return result;

        } finally {
            returnToPool(resource);
        }
    }


    private <T> T string2Bean(String sResult, Class<T> t) {
        if (sResult == null || sResult.length() <= 0 || t == null) {
            return null;
        }

        if (t == String.class) {
            return (T) sResult;
        }

        if (isPrimitiveWrapped(t)) {
            try {
                final Constructor<T> constructor = t.getConstructor(String.class);
                final Method valueOf = t.getMethod("valueOf", String.class);
                final Object invoke = valueOf.invoke(null, sResult);


                return (T) invoke;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        final JSONObject jsonObject = JSON.parseObject(sResult);

        final T result = JSONObject.toJavaObject(jsonObject, t);

        return result;
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


    private <T> boolean isPrimitiveWrapped(T t) {
        return isPrimitiveWrapped(t.getClass());
    }

    private <T> boolean isPrimitiveWrapped(Class<T> t) {

        return PRIMITIVE_TYPE_WRAPPED_SET.contains(t);
    }

}
