package net.xicp.tarbitrary.seckill.resolver;

import com.alibaba.druid.util.StringUtils;
import net.xicp.tarbitrary.seckill.constant.SysConstant;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
        final HttpServletResponse resp = webRequest.getNativeResponse(HttpServletResponse.class);
        String reqToken = (String) req.getAttribute(SysConstant.TOKEN);
        String cookieToken = getCookieToken(req, resp);

        if (StringUtils.isEmpty(reqToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }


        String realToken = cookieToken != null ? cookieToken : reqToken;

        final TradeUser userByToken = tradeUserService.getUserByToken(realToken);
        webRequest.setAttribute("tradeUser", userByToken, WebRequest.SCOPE_REQUEST);

        return userByToken;
    }

    private String getCookieToken(HttpServletRequest req, HttpServletResponse resp) {
        final Cookie[] cookies = req.getCookies();

        for (Cookie cookie : cookies) {
            if (Objects.equals(SysConstant.TOKEN, cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
