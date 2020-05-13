package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.dao.provider.GoodsProvider;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GoodsDao {
    @SelectProvider(value = GoodsProvider.class, method = "queryForList")
    public List<GoodsVO> queryForList();
}
