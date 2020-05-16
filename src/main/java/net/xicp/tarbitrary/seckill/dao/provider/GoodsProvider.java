package net.xicp.tarbitrary.seckill.dao.provider;


import org.apache.ibatis.jdbc.SQL;

public class GoodsProvider {


    public String queryForList() {
        final SQL sql = new SQL();
        sql.SELECT("t.id", "t.goods_name", "t.goods_title", "t.goods_img", "t.goods_detail", "t.goods_price", "t.goods_stock");
        sql.SELECT("r.stock_count", "r.start_date", "r.end_date", "r.seckill_price");
        sql.FROM("seckill_goods r").LEFT_OUTER_JOIN("goods t on r.goods_id=t.id");
        return sql.toString();
    }
}
