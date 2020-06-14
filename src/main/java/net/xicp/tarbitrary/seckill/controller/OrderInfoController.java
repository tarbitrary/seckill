package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.domain.Goods;
import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.result.Result;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.OrderInfoService;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
import net.xicp.tarbitrary.seckill.vo.OrderDetailVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (OrderInfo)表控制层
 *
 * @author tarbitrary
 * @since 2020-05-24 16:02:32
 */
@RestController
@RequestMapping("orderInfo")
public class OrderInfoController {
    /**
     * 服务对象
     */
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 服务对象
     */
    @Resource
    private SeckillOrderService seckillOrderService;


    @Resource
    private GoodsService goodsService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public OrderInfo selectOne(Long id) {
        return this.orderInfoService.queryById(id);
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(Model model, TradeUser user,
                                      @RequestParam("orderId") long orderId) {

        OrderInfo order = orderInfoService.queryById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        Goods goods = goodsService.getGoodsById(goodsId);
        final OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setOrder(order);
        orderDetailVO.setGoods(goods);
        return Result.success(orderDetailVO);
    }

}