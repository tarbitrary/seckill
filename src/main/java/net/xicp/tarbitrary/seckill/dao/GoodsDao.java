package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.dao.provider.GoodsProvider;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface GoodsDao {
    @SelectProvider(value = GoodsProvider.class, method = "queryForList")
    public List<GoodsVO> queryForList();

    @Select("select t.*, g.seckill_price, g.stock_count, g.start_date, g.end_date from goods t left outer join seckill_goods g on t.id=g.goods_id where t.id=#{id}")
    public GoodsVO getGoodsById(long id);
}
