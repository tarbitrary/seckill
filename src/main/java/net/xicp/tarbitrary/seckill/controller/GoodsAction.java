package net.xicp.tarbitrary.seckill.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.annotations.AccessLimit;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import net.xicp.tarbitrary.seckill.vo.GoodsDetailVO;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class GoodsAction {
    @Autowired
    private TradeUserService tradeUserService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/goods_list")
    @AccessLimit(seconds = 100, maxCount = 5)
    public String goodsList(TradeUser tradeUser, HttpServletResponse response, Model model) {
        log.info("tradeuser:{}", JSON.toJSONString(tradeUser));
        //model.addAttribute("tradeUser", tradeUser);

        final List<GoodsVO> goodsVOS = goodsService.queryForList();
        model.addAttribute("goodsList", goodsVOS);

        return "/goods/goods_list";
    }

    @RequestMapping(path = "/goods_detail/{goodsId}")
    public String goodsDetail(HttpServletRequest request, HttpServletResponse response,
                              Model model,
                              TradeUser tradeUser,
                              @PathVariable("goodsId") long goodsId) {
        final GoodsVO goodsById = goodsService.getGoodsById(goodsId);
        final Date startDate = goodsById.getStartDate();
        final Date endDate = goodsById.getEndDate();
        final long startTime = startDate.getTime();
        final long endTime =
                endDate.getTime();
        int seckillStatus, remainSeconds;
        long now = System.currentTimeMillis();
        if (now < startTime) {//秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }

        final GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        goodsDetailVO.setGoods(goodsById);
        goodsDetailVO.setTradeUser(tradeUser);

        model.addAttribute("user", tradeUser);
        model.addAttribute("goods", goodsById);

        model.addAttribute("goodsDetail", goodsDetailVO);
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds / 1000);

        return "/goods/goods_detail";
    }
}
