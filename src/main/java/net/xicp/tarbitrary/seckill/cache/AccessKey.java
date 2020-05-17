package net.xicp.tarbitrary.seckill.cache;

/**
 * 权限校验key
 */
public class AccessKey extends BaseKeyPrefix {
    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }
}
