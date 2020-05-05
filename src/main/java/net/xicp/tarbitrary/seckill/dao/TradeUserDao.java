package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.domain.TradeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TradeUserDao {
    /**
     * @param id
     * @return
     */
    @Select("select * from trade_user where id = #{id}")
    public TradeUser getUserById(@Param("id") Long id);

    public TradeUser getById(@Param("id") Long id);


    public int update(TradeUser tradeUser);
}
