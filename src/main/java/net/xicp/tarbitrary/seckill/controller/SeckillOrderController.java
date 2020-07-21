package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.annotations.AccessLimit;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.cache.init.GoodsInit;
import net.xicp.tarbitrary.seckill.domain.OrderInfo;
import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.mq.SecKillMessage;
import net.xicp.tarbitrary.seckill.mq.SecKillMessageSender;
import net.xicp.tarbitrary.seckill.result.CodeMsg;
import net.xicp.tarbitrary.seckill.result.Result;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * (SeckillOrder)表控制层
 *
 * @author tarbitrary
 * @since 2020-05-17 20:15:08
 */
@Controller
@RequestMapping("seckillOrder")
public class SeckillOrderController {
    /**
     * 服务对象
     */
    @Resource
    private SeckillOrderService seckillOrderService;


    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsInit goodsInit;

    @Resource
    private CacheService cacheService;

    @Resource
    private SecKillMessageSender sender;


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

    /**
     * load 12
     * tps 531
     *
     * @param tradeUser
     * @param goodsId
     * @param model
     * @return@
     */
    @RequestMapping("/doSeckill")
    @AccessLimit(seconds = 10, maxCount = 100)
    public String doSeckill(TradeUser tradeUser, @RequestParam("goodsId") long goodsId,
                            Model model) {
        //seckillOrderService.doSeckKill(goodsId);

        final GoodsVO goodsById = goodsService.getGoodsById(goodsId);
        if (null == goodsById) {
            model.addAttribute("errorMsg", CodeMsg.ORDER_NOT_EXIST);
            return "/order/seckill_fail";
        }

        if (goodsById.getGoodsStock() <= 0) {
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_FAIL);
            return "/order/seckill_fail";
        }

        final SeckillOrder seckillOrder = seckillOrderService.querySeckillOrderByUserIdAndGoodsId(tradeUser.getId(), goodsId);

        if (null != seckillOrder) {
            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA);
            return "/order/seckill_fail";
        }

        final OrderInfo orderInfo = seckillOrderService.doSeckill(tradeUser, goodsById);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsById);

        return "/order/order_detail";

    }

    @RequestMapping("/doSeckill1")
    @AccessLimit(seconds = 10, maxCount = 100)
    @ResponseBody
    public Result<OrderInfo> doSeckill1(TradeUser tradeUser, @RequestParam("goodsId") long goodsId,
                                        Model model) {

        final GoodsVO goodsById = goodsService.getGoodsById(goodsId);
        if (null == goodsById) {
            model.addAttribute("errorMsg", CodeMsg.ORDER_NOT_EXIST);
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        if (goodsById.getGoodsStock() <= 0) {
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_FAIL);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        final SeckillOrder seckillOrder = seckillOrderService.querySeckillOrderByUserIdAndGoodsId(tradeUser.getId(), goodsId);

        if (null != seckillOrder) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        final OrderInfo orderInfo = seckillOrderService.doSeckill(tradeUser, goodsById);

        return Result.success(orderInfo);

    }

    @RequestMapping("/doSeckillWithMQ")
    @AccessLimit(seconds = 10, maxCount = 100)
    @ResponseBody
    public Result<Integer> doSeckillWithMQ(TradeUser tradeUser, @RequestParam("goodsId") long goodsId,
                                           Model model) {


        final boolean goodsOver = goodsInit.goodsOver(goodsId);
        if (goodsOver) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        final Long decr = cacheService.decr(SeckillKey.GOODS_CACHE, goodsId + "");
        if (decr < 0) {
            goodsInit.flip2Over(goodsId);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }


        final SeckillOrder seckillOrder = seckillOrderService.querySeckillOrderByUserIdAndGoodsId(tradeUser.getId(), goodsId);

        if (null != seckillOrder) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        final SecKillMessage secKillMessage = new SecKillMessage();
        secKillMessage.setTradeUser(tradeUser);
        secKillMessage.setGoodsId(goodsId);

        sender.sendMessage(secKillMessage);

        return Result.success(0);
    }

    @RequestMapping("result")
    @ResponseBody
    public Result<Long> getResult(TradeUser tradeUser, @RequestParam("goodsId") long goodsId) {
        long result = seckillOrderService.getSecKillResult(tradeUser.getId(), goodsId);
        return Result.success(result);
    }


}