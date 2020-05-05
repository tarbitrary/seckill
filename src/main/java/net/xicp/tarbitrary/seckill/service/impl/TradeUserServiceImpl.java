package net.xicp.tarbitrary.seckill.service.impl;

import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.dao.TradeUserDao;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.exception.GlobalException;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import net.xicp.tarbitrary.seckill.util.MD5Util;
import net.xicp.tarbitrary.seckill.util.UUIDUtil;
import net.xicp.tarbitrary.seckill.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class TradeUserServiceImpl implements TradeUserService {
    @Resource
    private TradeUserDao tradeUserDao;

    @Autowired
    private CacheService cacheService;

    @Override
    public TradeUser getUserById(long id) {
        final TradeUser userById = tradeUserDao.getUserById(id);
        return userById;
    }

    @Override
    public TradeUser getUserByToken(String token) {
        return cacheService.get(SeckillKey.USER_INFO, token, TradeUser.class);
    }

    @Override
    public String login(LoginVO user) {
        Assert.notNull(user, "用户信息不允许为空");
        final TradeUser userById = getUserById(user.getMobile());
        if (null == userById) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        final String loginPassword = MD5Util.md5WithSalt(user.getPassword(), userById.getSalt());
        if (Objects.equals(loginPassword, userById.getPassword())) {
            final String token = UUIDUtil.uuid();
            cacheService.set(SeckillKey.USER_INFO, token, userById);
            return token;
        }

        throw new GlobalException(CodeMsg.PASSWORD_ERROR);
    }
}
