package net.xicp.tarbitrary.seckill.service.impl;

import net.xicp.tarbitrary.seckill.dao.SeckillOrderDao;
import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SeckillOrder)表服务实现类
 *
 * @author tarbitrary
 * @since 2020-05-17 20:15:07
 */
@Service("seckillOrderService")
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Resource
    private SeckillOrderDao seckillOrderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SeckillOrder queryById(Long id) {
        return this.seckillOrderDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SeckillOrder> queryAllByLimit(int offset, int limit) {
        return this.seckillOrderDao.queryAllByLimit(offset, limit);
    }


    /**
     * 新增数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public SeckillOrder insert(SeckillOrder seckillOrder) {
        this.seckillOrderDao.insert(seckillOrder);
        return seckillOrder;
    }

    /**
     * 修改数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public SeckillOrder update(SeckillOrder seckillOrder) {
        this.seckillOrderDao.update(seckillOrder);
        return this.queryById(seckillOrder.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.seckillOrderDao.deleteById(id) > 0;
    }

    @Override
    public SeckillOrder querySeckillOrderByUserIdAndGoodsId(Long id, long goodsId) {
        return seckillOrderDao.getOrderByUserIdAndGoodsId(id, goodsId);
    }
}