package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SeckillOrder)表数据库访问层
 *
 * @author tarbitrary
 * @since 2020-05-17 20:15:04
 */
public interface SeckillOrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SeckillOrder queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SeckillOrder> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param seckillOrder 实例对象
     * @return 对象列表
     */
    List<SeckillOrder> queryAll(SeckillOrder seckillOrder);

    /**
     * 新增数据
     *
     * @param seckillOrder 实例对象
     * @return 影响行数
     */
    int insert(SeckillOrder seckillOrder);

    /**
     * 修改数据
     *
     * @param seckillOrder 实例对象
     * @return 影响行数
     */
    int update(SeckillOrder seckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * get skill order by userId and goodsId
     *
     * @return
     */
    SeckillOrder getOrderByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    List<SeckillOrder> getAllSecKillOrdersByGoodsId(long goodsId);
}