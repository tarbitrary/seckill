package net.xicp.tarbitrary.seckill.controller;

import net.xicp.tarbitrary.seckill.domain.SeckillGoods;
import net.xicp.tarbitrary.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (SeckillGoods)表控制层
 *
 * @author tarbitrary
 * @since 2020-05-17 20:13:33
 */
@RestController
@RequestMapping("seckillGoods")
public class SeckillGoodsController {
    /**
     * 服务对象
     */
    @Resource
    private SeckillGoodsService seckillGoodsService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SeckillGoods selectOne(Long id) {
        return this.seckillGoodsService.queryById(id);
    }

}