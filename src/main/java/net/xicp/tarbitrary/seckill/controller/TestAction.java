package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.domain.Users;
import net.xicp.tarbitrary.seckill.result.Result;
import net.xicp.tarbitrary.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TestAction {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;

    @RequestMapping(path = "/hello")
    public HashMap<String, String> test() {
        final HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("name", "tarbitrary123tq13323");
        return stringStringHashMap;
    }

    @RequestMapping(path = "/user/add")
    public String userAdd() {
        final Users users = new Users();
        users.setName("tarbitrary");

        cacheService.set(SeckillKey.key1, "user", users);

        final int save = userService.save(users);
        return "save success";
    }

    @RequestMapping(path = "/user/getcache")
    public Users userGet() {


        final Users users = cacheService.get(SeckillKey.key1, "user", Users.class);

        return users;
    }

    @RequestMapping(path = "/user/{id}")
    public Result<Users> userAdd(@PathVariable("id") int id) {
        final Users byId = userService.getById(id);
        return Result.success(byId);
    }
}
