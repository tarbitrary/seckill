package net.xicp.tarbitrary.seckill.util;

import net.xicp.tarbitrary.seckill.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserUtil {
    private static CacheService cacheService;

    @Autowired
    public UserUtil(@Autowired CacheService initCacheService) {
        cacheService = initCacheService;
    }

    public static CacheService getCacheService() {
        return cacheService;
    }

//    @Autowired
//    public void setCacheService(CacheService cacheService1) {
//        cacheService = cacheService1;
//    }
}
