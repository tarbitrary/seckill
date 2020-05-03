package net.xicp.tarbitrary.seckill.cache;

public interface KeyPrefix {
    /**
     * 过期时间, 0代表永不过期
     * @return
     */
    int expireSeconds();

    /**
     * 前缀
     * @return
     */
    String getPrefix();
}
