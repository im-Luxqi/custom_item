package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.OcsUtil;
import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.domain.CusOrderInfo;
import com.duomai.project.product.adidasmusic.service.ICusOrderInfoService;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.taobao.api.response.OpenTradesSoldGetResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/* 获取订单
 * @description
 * @create by 王星齐
 * @time 2020-10-10 15:43:06
 **/
@Service
public class CusGetOrderExecute implements IApiExecute {

    @Resource
    private ICusOrderInfoService cusOrderInfoService;

    @Resource
    private ProjectHelper projectHelper;

    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        Assert.isTrue(OcsUtil.add(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick() + "CusGetOrderExecute", "_commit_", 1)
                , "10秒后重试");
//        projectHelper.checkoutMultipleCommit(sysParm, this);
        String openUId = sysParm.getApiParameter().getYunTokenParameter().getOpenUId();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        ActBaseSettingDto config = projectHelper.actBaseSettingFind();

        List<OpenTradesSoldGetResponse.Trade> goods = taobaoAPIService.taobaoOpenTradesSoldGet(openUId, TaoBaoTradeStatus.WAIT_SELLER_SEND_GOODS, config.getActStartTime(), config.getActEndTime());
        if (goods == null || goods.size() == 0)
            return YunReturnValue.fail("未获取到订单");

        //所有已保存的订单
        List<CusOrderInfo> dbCusOrders = cusOrderInfoService.getTidsByBuyerNick(buyerNick);
        StringBuilder hasUpdateTradeIds = new StringBuilder();
        dbCusOrders.forEach(x -> hasUpdateTradeIds.append(x.getTid()).append(","));

        /*符合条件的最新订单，同步最新订单*/
        List<OpenTradesSoldGetResponse.Trade> newestTrades = new ArrayList<>();
        for (OpenTradesSoldGetResponse.Trade trade : goods) {
            if (!hasUpdateTradeIds.toString().contains(trade.getTid()))
                newestTrades.add(trade);
        }
        if (newestTrades.size() > 0) {
            cusOrderInfoService.insertTaobaoTradeList(newestTrades, buyerNick, sysParm.getRequestStartTime());
        }
        return YunReturnValue.ok("获取当前用户订单!");
    }
}
