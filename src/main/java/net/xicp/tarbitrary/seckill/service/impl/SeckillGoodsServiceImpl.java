package net.xicp.tarbitrary.seckill.service.impl;

import net.xicp.tarbitrary.seckill.dao.SeckillGoodsDao;
import net.xicp.tarbitrary.seckill.domain.SeckillGoods;
import net.xicp.tarbitrary.seckill.service.SeckillGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SeckillGoods)表服务实现类
 *
 * @author tarbitrary
 * @since 2020-05-17 20:13:33
 */
@Service("seckillGoodsService")
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Resource
    private SeckillGoodsDao seckillGoodsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SeckillGoods queryById(Long id) {
        return this.seckillGoodsDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SeckillGoods> queryAllByLimit(int offset, int limit) {
        return this.seckillGoodsDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param seckillGoods 实例对象
     * @return 实例对象
     */
    @Override
    public SeckillGoods insert(SeckillGoods seckillGoods) {
        this.seckillGoodsDao.insert(seckillGoods);
        return seckillGoods;
    }

    /**
     * 修改数据
     *
     * @param seckillGoods 实例对象
     * @return 实例对象
     */
    @Override
    public SeckillGoods update(SeckillGoods seckillGoods) {
        this.seckillGoodsDao.update(seckillGoods);
        return this.queryById(seckillGoods.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.seckillGoodsDao.deleteById(id) > 0;
    }

    @Override
    public int reduceStock(Long id, Integer num) {
        return seckillGoodsDao.reduceStock(id, num);

    }
}