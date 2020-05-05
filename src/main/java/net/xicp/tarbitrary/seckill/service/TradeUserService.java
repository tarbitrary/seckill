package net.xicp.tarbitrary.seckill.service;

import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.vo.LoginVO;

/**
 * @author tarbitrary
 */
public interface TradeUserService {
     TradeUser getUserById(long id);

     TradeUser getUserByToken(String token);

     String login(LoginVO user);
}
