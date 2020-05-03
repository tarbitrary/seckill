package net.xicp.tarbitrary.seckill.cache;

public class SeckillKey extends BaseKeyPrefix {

    SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }



    public static final SeckillKey key1 = new SeckillKey(60, "seckill");
}
