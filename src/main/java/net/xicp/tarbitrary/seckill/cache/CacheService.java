package net.xicp.tarbitrary.seckill.cache;

/**
 * @author tarbitrary
 */
public interface CacheService {
    /**
     *
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
   public <T> boolean set(KeyPrefix prefix, String key, T value);

    /**
     *
     * @param prefix
     * @param key
     * @param t
     * @param <T>
     * @return
     */
   public <T> T get(KeyPrefix prefix, String key, Class<T> t);

    /**
     *
     * @param prefix
     * @param key
     * @return
     */
   public boolean exists(KeyPrefix prefix, String key);
}
