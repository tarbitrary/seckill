package net.xicp.tarbitrary.seckill.config;

import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.util.UserUtil;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class AutowiredStaticSmartInitializingSingleton implements SmartInitializingSingleton {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private AutowiredAnnotationBeanPostProcessor processor;

    @Autowired
    private CacheService cacheService;

    @Override
    public void afterSingletonsInstantiated() {
        processor.processInjection(new UserUtil(cacheService));
    }
}
