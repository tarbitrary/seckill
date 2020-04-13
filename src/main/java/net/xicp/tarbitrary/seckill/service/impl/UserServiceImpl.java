package net.xicp.tarbitrary.seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.dao.UserDao;
import net.xicp.tarbitrary.seckill.domain.Users;
import net.xicp.tarbitrary.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Users getById(int id) {
        log.debug("get by id {}", id);

        return userDao.getById(id);
    }

    @Override
    public int save(Users user) {
        log.debug("save users, detail {}", user.toString());
        return userDao.save(user);
    }
}
