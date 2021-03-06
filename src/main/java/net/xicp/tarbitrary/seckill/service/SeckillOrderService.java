package net.xicp.tarbitrary.seckill.service;

import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;

import java.util.List;

/**
 * (SeckillOrder)表服务接口
 *
 * @author tarbitrary
 * @since 2020-05-17 20:15:06
 */
public interface SeckillOrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SeckillOrder queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SeckillOrder> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    SeckillOrder insert(SeckillOrder seckillOrder);

    /**
     * 修改数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    SeckillOrder update(SeckillOrder seckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    SeckillOrder querySeckillOrderByUserIdAndGoodsId(Long id, Long goodsId);

    OrderInfo doSeckill(TradeUser user, GoodsVO goodsInfo);

    long getSecKillResult(long userId, long goodsId);
}