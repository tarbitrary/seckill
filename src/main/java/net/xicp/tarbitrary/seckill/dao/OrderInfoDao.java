package net.xicp.tarbitrary.seckill.dao;

import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (OrderInfo)表数据库访问层
 *
 * @author tarbitrary
 * @since 2020-05-24 16:02:32
 */
public interface OrderInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OrderInfo queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<OrderInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param orderInfo 实例对象
     * @return 对象列表
     */
    List<OrderInfo> queryAll(OrderInfo orderInfo);

    /**
     * 新增数据
     *
     * @param orderInfo 实例对象
     * @return 影响行数
     */
    int insert(OrderInfo orderInfo);

    /**
     * 修改数据
     *
     * @param orderInfo 实例对象
     * @return 影响行数
     */
    int update(OrderInfo orderInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}