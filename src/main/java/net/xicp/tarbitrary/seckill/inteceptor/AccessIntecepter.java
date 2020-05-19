package net.xicp.tarbitrary.seckill.inteceptor;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.annotations.AccessLimit;
import net.xicp.tarbitrary.seckill.cache.AccessKey;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.constant.SysConstant;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.exception.GlobalException;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限拦截校验器
 */

@Component
@Slf4j
public class AccessIntecepter implements HandlerInterceptor {

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

        if (!accessLimit.needLogin()) {
            return true;
        }

        String reqToken = (String) request.getAttribute(SysConstant.TOKEN);
        String cookieToken = getCookieToken(request, response);
        String realToken = cookieToken != null ? cookieToken : reqToken;

        if (StringUtils.isEmpty(realToken)) {
            toNeedLoginPage(request, response);
            return false;
        }

        final TradeUser userByToken = tradeUserService.getUserByToken(realToken);
        if (null == userByToken) {

            toNeedLoginPage(request, response);
            return false;
        }

        expireExtend(realToken, userByToken, response);
        request.setAttribute("tradeUser", userByToken);

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
                toAccessLimitPage(request, response, userByToken);
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

    private void toNeedLoginPage(HttpServletRequest request, HttpServletResponse response) {
        throw new GlobalException(CodeMsg.SESSION_ERROR);
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

    private void expireExtend(String realToken, TradeUser userByToken, HttpServletResponse resp) {
        log.info("expire time extend, token {}", realToken);
        tradeUserService.expireExtend(realToken, userByToken);
        final Cookie cookie = new Cookie(SysConstant.TOKEN, realToken);
        cookie.setMaxAge(SeckillKey.USER_INFO.getExpireSeconds());
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    private String getCookieToken(HttpServletRequest req, HttpServletResponse resp) {
        final Cookie[] cookies = req.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (Objects.equals(SysConstant.TOKEN, cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("post handler invoke {}, {}", request.getRequestURI(), handler);
    }
}
