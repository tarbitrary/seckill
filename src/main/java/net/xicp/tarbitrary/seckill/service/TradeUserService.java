package net.xicp.tarbitrary.seckill.service;

import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.vo.LoginVO;

/**
 * @author tarbitrary
 */
public interface TradeUserService {
     TradeUser getUserById(long id);

     TradeUser getUserByToken(String token);

     boolean expireExtend(String token, TradeUser user);

     String login(LoginVO user);
}
