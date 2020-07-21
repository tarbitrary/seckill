package net.xicp.tarbitrary.seckill.cache.init;

import lombok.extern.slf4j.Slf4j;
import net.xicp.tarbitrary.seckill.cache.CacheService;
import net.xicp.tarbitrary.seckill.cache.SeckillKey;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author tarbitrary
 * 商品初始化加载
 */
@Component
@Slf4j
public class GoodsInit implements CommandLineRunner {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CacheService cacheService;

    private static final Map<Long, Boolean> OVER_MAP = new ConcurrentHashMap<Long, Boolean>();
    private static Map<Long, GoodsVO> GOODS_MAP;

    @Override
    public void run(String... args) throws Exception {
        final List<GoodsVO> goodsVOS = goodsService.queryForList();

        if (CollectionUtils.isEmpty(goodsVOS)) {
            log.info("there is no goods stock for load, just skip");
            return;
        }
        goodsVOS.stream().forEach(s -> {
            cacheService.set(SeckillKey.GOODS_CACHE, s.getId() + "", s.getGoodsStock());
            OVER_MAP.put(s.getId(), Boolean.FALSE);
        });

        final Map<Long, GoodsVO> collect = goodsVOS.stream().collect(Collectors.toMap(keyMapper -> keyMapper.getId(), valueMapper -> valueMapper));
        GOODS_MAP = collect;

    }

    public boolean goodsOver(Long goodsId) {
        final Boolean result = OVER_MAP.get(goodsId);
        return result != null && result;
    }

    public void flip2Over(Long goodsId) {
        OVER_MAP.put(goodsId, Boolean.TRUE);
    }

    public long acquireGoodsStockOrigin(long goodsId) {
        final GoodsVO goodsVO = GOODS_MAP.get(goodsId);
        if (null != goodsVO) {
            return goodsVO.getGoodsStock();
        }

        return -1;
    }
}
