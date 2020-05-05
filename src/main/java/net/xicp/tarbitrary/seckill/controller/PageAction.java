package net.xicp.tarbitrary.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageAction {
    @GetMapping("/hello")
    public String hello() {
        return "redirect:/hello.html";
    }

    @GetMapping("/hello1")
    public String hello1() {
        return "/hello";
    }

    @GetMapping("/hello3")
    public String hello3() {
        return "/hello3";
    }

    @GetMapping("/hello4")
    public String hello4() {
        return "/hello3.html";
    }
}
