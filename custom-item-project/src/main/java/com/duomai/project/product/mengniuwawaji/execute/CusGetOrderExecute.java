package com.duomai.project.product.mengniuwawaji.execute;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.mengniuwawaji.domain.CusOrderInfo;
import com.duomai.project.product.mengniuwawaji.service.ICusOrderInfoService;
import com.taobao.api.response.OpenTradesSoldGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 获取订单
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-10-10 15:43:06
 */
@Component
public class CusGetOrderExecute implements IApiExecute {


    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Resource
    private ICusOrderInfoService cusOrderInfoService;

    @Resource
    private ProjectHelper projectHelper;

    @Resource
    private ITaobaoAPIService taobaoAPIService;

    public CusGetOrderExecute() {
    }

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {


        String openUId = sysParm.getApiParameter().getYunTokenParameter().getOpenUId();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        ActBaseSettingDto config = projectHelper.actBaseSettingFind();

        List<OpenTradesSoldGetResponse.Trade> goods = taobaoAPIService.taobaoOpenTradesSoldGet(openUId,
                TaoBaoTradeStatus.WAIT_SELLER_SEND_GOODS, config.getOrderStartTime(), config.getOrderEndTime());
        if (goods == null || goods.size() == 0) {
            return YunReturnValue.fail("未获取到订单");
        }

        //所有已保存的订单
        List<CusOrderInfo> dbCusOrders = cusOrderInfoService.getTidsByBuyerNick(buyerNick);
        StringBuilder hasUpdateTradeIds = new StringBuilder();
        dbCusOrders.forEach(x -> hasUpdateTradeIds.append(x.getTid()).append(","));

        /*符合条件的最新订单，同步最新订单*/
        StringBuilder tid = new StringBuilder();
        double allMoney = 0;
        List<OpenTradesSoldGetResponse.Trade> newestTrades = new ArrayList<>();
        for (OpenTradesSoldGetResponse.Trade trade : goods) {
            if (!("WAIT_SELLER_SEND_GOODS".equals(trade.getStatus()) || "SELLER_CONSIGNED_PART".equals(trade.getStatus())
                    || "WAIT_BUYER_CONFIRM_GOODS".equals(trade.getStatus()) || "TRADE_BUYER_SIGNED".equals(trade.getStatus())
                    || "TRADE_FINISHED".equals(trade.getStatus()))) {
                continue;
            }
            allMoney += Double.parseDouble(trade.getPayment());

            //未记录本地的订单
            if (!hasUpdateTradeIds.toString().contains(trade.getTid())) {
                newestTrades.add(trade);
            }
        }
        if (newestTrades.size() > 0) {
            cusOrderInfoService.insertTaobaoTradeList(newestTrades, buyerNick, sysParm.getRequestStartTime());
        }

        return YunReturnValue.ok("获取当前用户订单!");
    }
}
