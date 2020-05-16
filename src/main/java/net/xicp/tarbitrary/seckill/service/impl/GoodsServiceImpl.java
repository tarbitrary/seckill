package net.xicp.tarbitrary.seckill.service.impl;

import net.xicp.tarbitrary.seckill.dao.GoodsDao;
import net.xicp.tarbitrary.seckill.service.GoodsService;
import net.xicp.tarbitrary.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tarbitrary
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsVO> queryForList() {
        return goodsDao.queryForList();
    }

    @Override
    public GoodsVO getGoodsById(long goodsId) {
        return goodsDao.getGoodsById(goodsId);
    }
}
