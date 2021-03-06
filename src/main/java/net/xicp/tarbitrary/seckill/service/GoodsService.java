package net.xicp.tarbitrary.seckill.service;

import net.xicp.tarbitrary.seckill.vo.GoodsVO;

import java.util.List;

/**
 * @author tarbitrary
 */
public interface GoodsService {
    List<GoodsVO> queryForList();

    GoodsVO getGoodsById(long goodsId);
}
