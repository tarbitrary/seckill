package net.xicp.tarbitrary.seckill.cache;


/**
 * @author tarbitrary
 */
public class PageKey extends BaseKeyPrefix {
    public PageKey(int expireSeconds, String keyPrefix) {
        super(expireSeconds, keyPrefix);
    }

    public static final PageKey PAGE_LIST = new PageKey(60, "pagelist");
    public static final PageKey GOODS_INFO = new PageKey(60, "goodsInfo");
}
