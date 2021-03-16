package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;
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
    @Autowired
    private FinishTheTaskHelper finishTheTaskHelper;

    public CusGetOrderExecute() {
    }

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {


        String openUId = sysParm.getApiParameter().getYunTokenParameter().getOpenUId();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        ActBaseSettingDto config = projectHelper.actBaseSettingFind();

        List<OpenTradesSoldGetResponse.Trade> goods = taobaoAPIService.taobaoOpenTradesSoldGet(openUId, TaoBaoTradeStatus.WAIT_SELLER_SEND_GOODS, config.getActStartTime(), config.getActEndTime());
        if (goods == null || goods.size() == 0) {
            return YunReturnValue.fail("未获取到订单");
        }

        //所有已保存的订单
        List<CusOrderInfo> dbCusOrders = cusOrderInfoService.getTidsByBuyerNick(buyerNick);
        StringBuilder hasUpdateTradeIds = new StringBuilder();
        dbCusOrders.forEach(x -> hasUpdateTradeIds.append(x.getTid()).append(","));

        /*符合条件的最新订单，同步最新订单*/
        StringBuilder tid = new StringBuilder();
        int sendTime = 0;
        List<OpenTradesSoldGetResponse.Trade> newestTrades = new ArrayList<>();
        for (OpenTradesSoldGetResponse.Trade trade : goods) {
            if (!("WAIT_SELLER_SEND_GOODS".equals(trade.getStatus()) || "SELLER_CONSIGNED_PART".equals(trade.getStatus())
                    || "WAIT_BUYER_CONFIRM_GOODS".equals(trade.getStatus()) || "TRADE_BUYER_SIGNED".equals(trade.getStatus())
                    || "TRADE_FINISHED".equals(trade.getStatus()))) {
                continue;
            }

            if (!hasUpdateTradeIds.toString().contains(trade.getTid())) {

//                double cunzhenMoney = 0;
//                List<OpenTradesSoldGetResponse.Order> orders = trade.getOrders();
//                if (!CollectionUtils.isEmpty(orders)) {
//                    for (OpenTradesSoldGetResponse.Order order : orders) {
//                        if (order.getTitle().contains("纯甄")) {
//                            cunzhenMoney += Double.parseDouble(order.getPayment());
//                        }
//                    }
//                }
//                Double taskOrderShouldSpend = 12.00;
//                if (cunzhenMoney >= Double.valueOf(taskOrderShouldSpend)) {
//                    tid.append(trade.getTid() + ",");
//                    sendTime += 3;
//                }
                tid.append(trade.getTid() + ",");
                newestTrades.add(trade);
            }
        }
        if (newestTrades.size() == 0) {
            return YunReturnValue.fail("当前没有可用的新订单");
        }

        cusOrderInfoService.insertTaobaoTradeList(newestTrades, buyerNick, sysParm.getRequestStartTime());

        /*今日已参与次数*/
        long todayHasJoin = luckyDrawHelper.countTodayLuckyChanceFrom(buyerNick, LuckyChanceFromEnum.ORDER);
        if (todayHasJoin > 0) {
            todayHasJoin = todayHasJoin / 5;
        }

        /*需要新发卡片次数*/
        Integer taskOrderFront = 3;//每日前3次有卡牌奖品
        Integer shouldNewSend = 0;
        for (int i = 1; i <= newestTrades.size(); i++) {
            todayHasJoin++;
            if (todayHasJoin > 3) {
                todayHasJoin--;
                break;
            }
            shouldNewSend += 5;
        }

        if (shouldNewSend > 0) {
            luckyDrawHelper.sendCard(buyerNick, LuckyChanceFromEnum.ORDER, shouldNewSend,
                    "消费任务，获得【有料品鉴官】一博送你的食力拼图*" + shouldNewSend,tid.toString());
            SysTaskDailyBoard taskDailyBoard = finishTheTaskHelper.todayTaskBoard(buyerNick);
            taskDailyBoard.setSpendProgress("(" + todayHasJoin + "/" + taskOrderFront + ")");
            finishTheTaskHelper.updateTaskBoard(taskDailyBoard);
        }
        return YunReturnValue.ok("获取当前用户订单!");
    }
}
