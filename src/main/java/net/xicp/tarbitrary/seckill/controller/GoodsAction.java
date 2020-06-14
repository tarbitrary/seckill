package net.xicp.tarbitrary.seckill.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.annotations.AccessLimit;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.PageKey;
import net.xicp.tarbitrary.seckill.domain.TradeUser;
import net.xicp.tarbitrary.seckill.result.Result;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.service.TradeUserService;
import net.xicp.tarbitrary.seckill.vo.GoodsDetailVO;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
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

    @Resource(name = "thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;

    @Resource
    private ContentNegotiatingViewResolver viewResolver;

    @GetMapping(value = "/goods_list", produces = "text/html")
    @AccessLimit(seconds = 100, maxCount = Integer.MAX_VALUE)
    @ResponseBody
    public String goodsList(TradeUser tradeUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        log.info("tradeuser:{}", JSON.toJSONString(tradeUser));
        //model.addAttribute("tradeUser", tradeUser);

        final String result = cacheService.get(PageKey.PAGE_LIST, "", String.class);

        if (!StringUtils.isEmpty(result)) {
            return result;
        }

        final List<GoodsVO> goodsVOS = goodsService.queryForList();
        model.addAttribute("goodsList", goodsVOS);

        final WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());

        final String process = thymeleafViewResolver.getTemplateEngine().process("/goods/goods_list", webContext);
        if (!StringUtils.isEmpty(process)) {
            cacheService.set(PageKey.PAGE_LIST, "", process);
        }
        return process;

//        final ModelAndView modelAndView = new ModelAndView("/goods/goods_list");
//
//        return modelAndView;
    }

    @RequestMapping(path = "/goods_detail1/{goodsId}", produces = "text/html")
    @ResponseBody
    public String goodsDetail(HttpServletRequest request, HttpServletResponse response,
                              Model model,
                              TradeUser tradeUser,
                              @PathVariable("goodsId") long goodsId) {

        final String result = cacheService.get(PageKey.GOODS_INFO, "" + goodsId, String.class);

        if (!StringUtils.isEmpty(result)) {
            return result;
        }


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

        final WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());

        final String process = thymeleafViewResolver.getTemplateEngine().process("/goods/goods_detail", webContext);
        if (!StringUtils.isEmpty(process)) {
            cacheService.set(PageKey.GOODS_INFO, "" + goodsId, process);
        }

        return process;
    }


    @RequestMapping(path = "/goods_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> getGoodsDetail(HttpServletRequest request, HttpServletResponse response,
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
        goodsDetailVO.setRemainSeconds(remainSeconds);
        goodsDetailVO.setSeckillStatus(seckillStatus);

        return Result.success(goodsDetailVO);
    }
}
