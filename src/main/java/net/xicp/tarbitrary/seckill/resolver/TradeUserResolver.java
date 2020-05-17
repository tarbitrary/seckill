package net.xicp.tarbitrary.seckill.resolver;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TradeUserResolver implements HandlerMethodArgumentResolver {


    private TradeUserService tradeUserService;

    public TradeUserResolver(TradeUserService tradeUserService) {
        this.tradeUserService = tradeUserService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return TradeUser.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        final Object tradeUser = req.getAttribute("tradeUser");
//        String reqToken = (String) req.getAttribute(SysConstant.TOKEN);
//        String cookieToken = getCookieToken(req, resp);
//
//        if (StringUtils.isEmpty(reqToken) && StringUtils.isEmpty(cookieToken)) {
//            return null;
//        }
//
//
//        String realToken = cookieToken != null ? cookieToken : reqToken;
//
//        final TradeUser userByToken = tradeUserService.getUserByToken(realToken);
//        if (null != userByToken) {
//            expireExtend(realToken, userByToken, resp);
//        }
//        webRequest.setAttribute("tradeUser", userByToken, WebRequest.SCOPE_REQUEST);

        return tradeUser;
    }

//    private void expireExtend(String realToken, TradeUser userByToken, HttpServletResponse resp) {
//        log.info("expire time extend, token {}", realToken);
//        tradeUserService.expireExtend(realToken, userByToken);
//        final Cookie cookie = new Cookie(SysConstant.TOKEN, realToken);
//        cookie.setMaxAge(SeckillKey.USER_INFO.getExpireSeconds());
//        cookie.setPath("/");
//        resp.addCookie(cookie);
//    }
//
//    private String getCookieToken(HttpServletRequest req, HttpServletResponse resp) {
//        final Cookie[] cookies = req.getCookies();
//        if (ArrayUtils.isEmpty(cookies)) {
//            return null;
//        }
//
//        for (Cookie cookie : cookies) {
//            if (Objects.equals(SysConstant.TOKEN, cookie.getName())) {
//                return cookie.getValue();
//            }
//        }
//
//        return null;
//    }
}
