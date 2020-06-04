package net.xicp.tarbitrary.seckill.config;

import net.xicp.tarbitrary.seckill.inteceptor.AccessInterceptor;
import net.xicp.tarbitrary.seckill.resolver.TradeUserResolver;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TradeUserResolver tradeUserResolver;

    @Resource
    private AccessInterceptor accessIntecepter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tradeUserResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessIntecepter);
    }


    @Bean
    public TradeUserResolver tradeUserResolver(TradeUserService tradeUserService) {
        return new TradeUserResolver(tradeUserService);
    }
}
