package net.xicp.tarbitrary.seckill.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
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
    public String goodsList(TradeUser tradeUser, HttpServletResponse response, Model model) {
        log.info("tradeuser:{}", JSON.toJSONString(tradeUser));
        //model.addAttribute("tradeUser", tradeUser);

        final List<GoodsVO> goodsVOS = goodsService.queryForList();
        model.addAttribute("goodsList", goodsVOS);

        return "/goods/goods_list";
    }
}
