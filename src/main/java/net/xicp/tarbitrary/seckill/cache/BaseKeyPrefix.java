package net.xicp.tarbitrary.seckill.cache;

import lombok.Data;

/**
 * @author tarbitrary
 */
@Data
public abstract class BaseKeyPrefix implements KeyPrefix {
    private int expireSeconds;

    private String keyPrefix;

    public BaseKeyPrefix(String keyPrefix) {
        this(0, keyPrefix);
    }

    public BaseKeyPrefix(int expireSeconds, String keyPrefix){
        this.expireSeconds = expireSeconds;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public String buildKey(String key) {
        return getPrefix() + ":" + key;
    }

    @Override
    public int expireSeconds() {
        return getExpireSeconds();
    }

    @Override
    public String getPrefix() {
        final String simpleName = getClass().getSimpleName();
        return simpleName + ":" + getKeyPrefix();
    }
}
