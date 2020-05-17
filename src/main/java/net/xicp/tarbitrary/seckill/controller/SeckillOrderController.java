package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.domain.SeckillOrder;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.service.SeckillOrderService;
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

    public String doSeckill(TradeUser tradeUser, @RequestParam("goodsId") long goodsId) {

        return "/goods_list/list";

    }

}