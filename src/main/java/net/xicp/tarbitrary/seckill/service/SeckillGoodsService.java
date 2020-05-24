package net.xicp.tarbitrary.seckill.service;

import net.xicp.tarbitrary.seckill.domain.SeckillGoods;

import java.util.List;

/**
 * (SeckillGoods)表服务接口
 *
 * @author tarbitrary
 * @since 2020-05-17 20:13:33
 */
public interface SeckillGoodsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SeckillGoods queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SeckillGoods> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param seckillGoods 实例对象
     * @return 实例对象
     */
    SeckillGoods insert(SeckillGoods seckillGoods);

    /**
     * 修改数据
     *
     * @param seckillGoods 实例对象
     * @return 实例对象
     */
    SeckillGoods update(SeckillGoods seckillGoods);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    int reduceStock(Long id, Integer num);
}