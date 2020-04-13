package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.domain.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Select("select * from users where id = #{id}")
    public Users getById(@Param("id") int id);

    @Insert("insert into users(name) values (#{name})")
    public int save(Users user);
}
