package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.annotations.AccessLimit;
import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (SeckillOrder)表控制层
 *
 * @author tarbitrary
 * @since 2020-05-17 20:15:08
 */
@RestController
@RequestMapping("seckillOrder")
public class SeckillOrderController {
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
    public SeckillOrder selectOne(Long id) {
        return this.seckillOrderService.queryById(id);
    }

    @RequestMapping("/doSeckill")
    @AccessLimit(seconds = 10, maxCount = 100)
    public String doSeckill(TradeUser tradeUser, @RequestParam("goodsId") long goodsId,
                            Model model) {
        //seckillOrderService.doSeckKill(goodsId);

        final GoodsVO goodsById = goodsService.getGoodsById(goodsId);
        if (null == goodsById) {
            model.addAttribute("errorMsg", CodeMsg.ORDER_NOT_EXIST);
            return "/goods_list/seckill_fail";
        }

        if (goodsById.getGoodsStock() <= 0) {
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_FAIL);
            return "/goods_list/seckill_fail";
        }

        final SeckillOrder seckillOrder = seckillOrderService.querySeckillOrderByUserIdAndGoodsId(tradeUser.getId(), goodsId);

        if (null != seckillOrder) {
            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA);
            return "/goods_list/seckill_fail";
        }

        final OrderInfo orderInfo = seckillOrderService.doSeckill(tradeUser, goodsById);
        model.addAttribute("orderInfo", orderInfo);

        return "/goods_list/list";

    }

}