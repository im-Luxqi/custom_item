package com.duomai.project.product.mengniuwawaji.service;

import com.duomai.common.framework.mybatisplus.service.BaseService;
import com.duomai.project.product.mengniuwawaji.domain.CusOrderInfo;
import com.taobao.api.response.OpenTradesSoldGetResponse;

import java.util.Date;
import java.util.List;

/**
 * 订单 服务层
 *
 * @author system
 */
public interface ICusOrderInfoService extends BaseService<CusOrderInfo> {


    List<CusOrderInfo> getTidsByBuyerNick(String buyerNick);
    Integer countTidsByBuyerNick(String buyerNick);

    void insertTaobaoTradeList(List<OpenTradesSoldGetResponse.Trade> newestTrades, String buyerNick, Date requestStartTime);
}
