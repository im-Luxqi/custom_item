package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.api.taobao.enums.TaoBaoTradeStatus;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.mengniuwawaji.domain.CusOrderInfo;
import com.duomai.project.product.mengniuwawaji.service.ICusOrderInfoService;
import com.taobao.api.response.OpenTradesSoldGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 游戏首页 小彩蛋
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexEggExecute implements IApiExecute {

    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private ProjectHelper projectHelper;

    @Resource
    private ICusOrderInfoService cusOrderInfoService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        String openUId = sysParm.getApiParameter().getYunTokenParameter().getOpenUId();

        ActBaseSettingDto config = projectHelper.actBaseSettingFind();
        List<OpenTradesSoldGetResponse.Trade> goods = taobaoAPIService.taobaoOpenTradesSoldGet(openUId,
                TaoBaoTradeStatus.WAIT_SELLER_SEND_GOODS, config.getOrderStartTime(), config.getOrderEndTime());
        double allMoney = 0;
        if (goods != null && goods.size() > 0) {
            //所有已保存的订单
            List<CusOrderInfo> dbCusOrders = cusOrderInfoService.getTidsByBuyerNick(buyerNick);
            StringBuilder hasUpdateTradeIds = new StringBuilder();
            dbCusOrders.forEach(x -> hasUpdateTradeIds.append(x.getTid()).append(","));
            /*符合条件的最新订单，同步最新订单*/
            StringBuilder tid = new StringBuilder();
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
        }

        //1.是否邀请过好友
        resultMap.put("have_invite", BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveInviteFriend()));
        //2.是否浏览过商品
        resultMap.put("have_browse", BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveBrowseGoods()));
        //3.是否购买过coach商品
        resultMap.put("have_spend", allMoney > 0);
        //4.是否加入会员
        resultMap.put("have_member", taobaoAPIService.isMember(buyerNick));
        return YunReturnValue.ok(resultMap, "游戏首页 小彩蛋");
    }
}




