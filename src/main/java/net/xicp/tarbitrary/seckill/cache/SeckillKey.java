package net.xicp.tarbitrary.seckill.cache;

/**
 * @author tarbitrary
 */
public class SeckillKey extends BaseKeyPrefix {

    SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static final SeckillKey key1 = new SeckillKey(60, "seckill");
    public static final SeckillKey USER_INFO = new SeckillKey(60 * 30, "userinfo");
    public static final SeckillKey GOODS_CACHE = new SeckillKey(0, "goodsCache");
    public static final SeckillKey URL_CACHE = new SeckillKey(60, "urlCache");
    public static final SeckillKey IMAGE_CACHE = new SeckillKey(300, "imageCache");
}
