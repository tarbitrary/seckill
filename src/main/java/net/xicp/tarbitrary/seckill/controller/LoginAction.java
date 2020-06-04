package net.xicp.tarbitrary.seckill.controller;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.annotations.NoLogin;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.result.Result;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import net.xicp.tarbitrary.seckill.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

@Controller
@Slf4j
public class LoginAction {

    @Autowired
    private TradeUserService tradeUserService;

    @Autowired
    private Validator validator;

    @GetMapping("/login")
    @NoLogin
    public String login() {
        return "/user/login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    @NoLogin
    public Result<String> doLogin(HttpServletResponse response, @Validated LoginVO loginVO) {
        log.info("user {} login", loginVO.getMobile());
        final String token = tradeUserService.login(loginVO);

        addToken2Cookie(token, response);

        return Result.success(token);

    }

    private void addToken2Cookie(String token, HttpServletResponse response) {
        final Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(SeckillKey.USER_INFO.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @GetMapping("/ex2")
    public void doTest() {
        throw new RuntimeException("test web error");
    }


}
