package net.xicp.tarbitrary.seckill.service.impl;

import net.xicp.tarbitrary.seckill.cache.init.GoodsInit;
import net.xicp.tarbitrary.seckill.dao.SeckillOrderDao;
import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.exception.GlobalException;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.service.OrderInfoService;
import net.xicp.tarbitrary.seckill.service.SeckillGoodsService;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Resource
    private GoodsInit goodsInit;

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
    public SeckillOrder querySeckillOrderByUserIdAndGoodsId(Long id, Long goodsId) {
        return seckillOrderDao.getOrderByUserIdAndGoodsId(id, goodsId);
    }

    @Override
    @Transactional
    public OrderInfo doSeckill(TradeUser user, GoodsVO goodsInfo) {
        //reduce sock
        final int result = seckillGoodsService.reduceStock(goodsInfo.getSeckillGoodsId(), 1);
        if (result <= 0) {
            throw new GlobalException(CodeMsg.MIAO_SHA_OVER);
        }

        //persist order
        OrderInfo orderInfo = buildOrderInfo(user, goodsInfo);
        orderInfoService.insert(orderInfo);

        //persist seckill order
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsInfo.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderDao.insert(seckillOrder);


        return orderInfo;
    }

    private OrderInfo buildOrderInfo(TradeUser user, GoodsVO goodsInfo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsInfo.getId());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsName(goodsInfo.getGoodsName());
        orderInfo.setPayDate(new Date());
        orderInfo.setStatus(0);
        orderInfo.setGoodsPrice(goodsInfo.getSeckillPrice());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setUserId(user.getId());
        return orderInfo;
    }

    public long getSecKillResult(long userId, long goodsId) {
        final SeckillOrder order = seckillOrderDao.getOrderByUserIdAndGoodsId(userId, goodsId);

        if (order != null) {//秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = goodsInit.goodsOver(goodsId);
            if (!isOver) {//此商品的秒杀还没结束，返回处理中
                return 0;
            }

            //此商品的秒杀已经结束，但是可能订单还在生成中
            //获取所有的秒杀订单, 判断订单数量和参与秒杀的商品数量
            List<SeckillOrder> orders = seckillOrderDao.getAllSecKillOrdersByGoodsId(goodsId);
            if (orders == null || orders.size() < goodsInit.acquireGoodsStockOrigin(goodsId)) {
                return 0;//订单还在生成中
            }
            //判断是否有此用户的订单
            SeckillOrder o = fetchOrderByUserId(orders, userId);
            if (o != null) {//如果有，则说明秒杀成功
                return o.getOrderId();
            } else {//秒杀失败
                return -1;
            }
        }
    }

    private SeckillOrder fetchOrderByUserId(List<SeckillOrder> orders, long userId) {
        if (orders == null || orders.size() <= 0) {
            return null;
        }
        for (SeckillOrder order : orders) {
            if (order.getUserId().equals(userId)) {
                return order;
            }
        }
        return null;
    }
}