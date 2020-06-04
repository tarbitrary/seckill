package net.xicp.tarbitrary.seckill.inteceptor;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.annotations.NoLogin;
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

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tarbitrary
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private static final ConcurrentHashMap<HandlerMethod, NoLogin> NO_LOGIN_MAP = new ConcurrentHashMap<HandlerMethod, NoLogin>();
    private static final ConcurrentHashMap<HandlerMethod, Boolean> NEED_LOGIN_MAP = new ConcurrentHashMap<HandlerMethod, Boolean>();

    @Resource
    private TradeUserService tradeUserService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            return doLoginCheck(request, response, (HandlerMethod) handler);
        }

        return true;
    }

    private boolean doLoginCheck(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        if (NEED_LOGIN_MAP.contains(handler)) {
            return checkToken(request, response, handler);
        }

        if (NO_LOGIN_MAP.contains(handler)) {
            return true;
        }

        final NoLogin methodAnnotation = handler.getMethodAnnotation(NoLogin.class);
        if (null != methodAnnotation) {
            NO_LOGIN_MAP.put(handler, methodAnnotation);
            return true;
        }

        NEED_LOGIN_MAP.put(handler, Boolean.TRUE);

        return checkToken(request, response, handler);
    }

    private boolean checkToken(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        String reqToken = (String) request.getParameter(SysConstant.TOKEN);
        String cookieToken = getCookieToken(request, response);
        String realToken = cookieToken != null ? cookieToken : reqToken;

        if (StringUtils.isEmpty(realToken)) {
            log.warn("invalid request {} detected by login check ", request.getRequestURI());
            throw new GlobalException(CodeMsg.SESSION_ERROR);
        }


        final TradeUser userByToken = tradeUserService.getUserByToken(realToken);
        if (null == userByToken) {
            log.warn("invalid token  detected by login check, token {} ", realToken);
            throw new GlobalException(CodeMsg.SESSION_ERROR);
        }

        expireExtend(realToken, userByToken, response);

        request.setAttribute("tradeUser", userByToken);
        request.setAttribute(SysConstant.TOKEN, realToken);

        return true;
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

    private void toNeedLoginPage(HttpServletRequest request, HttpServletResponse response) {
        throw new GlobalException(CodeMsg.SESSION_ERROR);
    }

    private void expireExtend(String realToken, TradeUser userByToken, HttpServletResponse resp) {
        log.info("expire time extend, token {}", realToken);
        tradeUserService.expireExtend(realToken, userByToken);
        final Cookie cookie = new Cookie(SysConstant.TOKEN, realToken);
        cookie.setMaxAge(SeckillKey.USER_INFO.getExpireSeconds());
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

}
