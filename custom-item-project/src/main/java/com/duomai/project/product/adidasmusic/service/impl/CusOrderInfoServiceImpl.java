package com.duomai.project.product.adidasmusic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.duomai.common.framework.mybatisplus.service.impl.BaseServiceImpl;
import com.duomai.project.product.adidasmusic.domain.CusOrderInfo;
import com.duomai.project.product.adidasmusic.mapper.CusOrderInfoMapper;
import com.duomai.project.product.adidasmusic.service.ICusOrderInfoService;
import com.duomai.project.tool.CommonDateParseUtil;
import com.taobao.api.response.OpenTradesSoldGetResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单 服务层实现
 *
 * @author system
 */
@Service
public class CusOrderInfoServiceImpl extends BaseServiceImpl<CusOrderInfoMapper, CusOrderInfo> implements ICusOrderInfoService {

    @Override
    public List<CusOrderInfo> getTidsByBuyerNick(String buyerNick) {
        return this.list(Wrappers.<CusOrderInfo>query().select("distinct tid").eq("buyer_nick", buyerNick));
    }

    @Override
    public Integer countTidsByBuyerNick(String buyerNick) {
        return this.count(Wrappers.<CusOrderInfo>query().select("distinct tid").eq("buyer_nick", buyerNick));
    }

    @Transactional
    @Override
    public void insertTaobaoTradeList(List<OpenTradesSoldGetResponse.Trade> newestTrades, String buyerNick, Date requestStartTime) {
        List<CusOrderInfo> adPreOrderInfoList = new ArrayList<>();
        newestTrades.forEach((trade) -> {
            for (OpenTradesSoldGetResponse.Order order : trade.getOrders()) {
                CusOrderInfo cusOrderInfo = new CusOrderInfo();
                cusOrderInfo.setId(UUID.randomUUID().toString());
                cusOrderInfo.setCreateTime(requestStartTime);
                cusOrderInfo.setTitle(order.getTitle());//商品名称
                cusOrderInfo.setPicUrl(order.getPicPath());//商品图片
                cusOrderInfo.setNumId(order.getNumIid());//商品Id
                cusOrderInfo.setOrderPrice(order.getPayment());//订单价格
                cusOrderInfo.setOrderTime(CommonDateParseUtil.string2date(trade.getPayTime(), "yyyy-MM-dd"));//下单时间
                cusOrderInfo.setTid(trade.getTid());//订单Id
                cusOrderInfo.setBuyerNick(buyerNick);
                adPreOrderInfoList.add(cusOrderInfo);
            }
        });
        this.saveBatch(adPreOrderInfoList);
    }
}
