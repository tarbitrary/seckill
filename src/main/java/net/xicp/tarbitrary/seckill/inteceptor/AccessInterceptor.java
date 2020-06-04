package net.xicp.tarbitrary.seckill.inteceptor;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.annotations.AccessLimit;
import net.xicp.tarbitrary.seckill.cache.AccessKey;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.constant.SysConstant;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.exception.GlobalException;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限拦截校验器
 *
 * @author tarbitrary
 */

@Component
@Slf4j
public class AccessInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<HandlerMethod, AccessLimit> ACCESS_MAP = new ConcurrentHashMap<HandlerMethod, AccessLimit>();

    @Resource
    private ContentNegotiatingViewResolver viewResolver;

    @Resource
    private TradeUserService tradeUserService;

    @Resource
    private CacheService cacheService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        log.info("pre handle {}", request.getRequestURI());

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Method method = handlerMethod.getMethod();


        final AccessLimit accessLimit = getAccessLimit(handlerMethod);
        if (null == accessLimit) {
            return true;
        }


        TradeUser tradeUser = (TradeUser) request.getAttribute("tradeUser");
        String realToken = (String) request.getAttribute(SysConstant.TOKEN);


        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        String key = buildKey(request, realToken);
        final AccessKey accessKey = AccessKey.withExpire(seconds);
        final Integer counts = cacheService.get(accessKey, key, Integer.class);
        if (null == counts) {
            cacheService.set(accessKey, key, 1);
        } else {
            if (counts >= accessLimit.maxCount()) {
                //限流操作
                toAccessLimitPage(request, response, tradeUser);
                return false;
            }
        }


        cacheService.incr(accessKey, key);


        return true;
    }

    private void toAccessLimitPage(HttpServletRequest request, HttpServletResponse response, TradeUser userByToken) {
        log.info("access limit, {}", userByToken.getId());
        throw new GlobalException(CodeMsg.ACCESS_LIMIT_REACHED);
    }

    private String buildKey(HttpServletRequest request, String userByToken) {
        return request.getRequestURI() + "_" + userByToken;
    }


    private AccessLimit getAccessLimit(HandlerMethod handlerMethod) {
        final AccessLimit accessLimit = ACCESS_MAP.get(handlerMethod);
        if (null != accessLimit) {
            return accessLimit;
        }

        final AccessLimit methodAnnotation = handlerMethod.getMethodAnnotation(AccessLimit.class);
        if (null != methodAnnotation) {
            ACCESS_MAP.put(handlerMethod, methodAnnotation);
            return methodAnnotation;
        }


        return null;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("post handler invoke {}, {}", request.getRequestURI(), handler);
    }
}
