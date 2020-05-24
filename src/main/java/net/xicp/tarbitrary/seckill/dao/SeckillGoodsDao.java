package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.domain.SeckillGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * (SeckillGoods)表数据库访问层
 *
 * @author tarbitrary
 * @since 2020-05-17 20:13:33
 */
public interface SeckillGoodsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SeckillGoods queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SeckillGoods> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param seckillGoods 实例对象
     * @return 对象列表
     */
    List<SeckillGoods> queryAll(SeckillGoods seckillGoods);

    /**
     * 新增数据
     *
     * @param seckillGoods 实例对象
     * @return 影响行数
     */
    int insert(SeckillGoods seckillGoods);

    /**
     * 修改数据
     *
     * @param seckillGoods 实例对象
     * @return 影响行数
     */
    int update(SeckillGoods seckillGoods);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * reduce stock
     *
     * @param id  id
     * @param num reduce stock num
     * @return
     */
    @Update("update seckill_goods set stock_count = stock_count - #{num} where id = #{id} and stock_count >= #{num}")
    int reduceStock(@Param("id") Long id, @Param("num") int num);

}