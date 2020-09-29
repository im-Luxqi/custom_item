package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 非法获取抽奖次数
 * @description (哪个渠道，页面)
 * @create by 王星齐
 * @time 2020-08-26 19:11:50
 **/
@Component
public class IllegalAccessLuckyChanceExecute implements IApiExecute {
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Assert.isTrue(luckyDrawHelper.findMaxWinGoodNum() > 10, "？？？？？，想屁吃呢");
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = jsonObjectAdmjson.getString("buyerNick");
        Assert.hasLength(buyerNick, "buyernick不能为空");
        luckyDrawHelper.sendLuckyChance(buyerNick, LuckyChanceFromEnum.DAKA, 30, null);
        return YunReturnValue.ok("恭喜【" + buyerNick + "】获取30次抽奖机会");
    }
}