package net.xicp.tarbitrary.seckill.service;

import net.xicp.tarbitrary.seckill.domain.Users;

public interface UserService {

    public Users getById(int id);


    public int save(Users user);
}
