package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法获取抽奖次数
 *
 * @author im-luxqi
 * @description (哪个渠道 ， 页面)
 * @create by 王星齐
 * @time 2020-08-26 19:11:50
 */
@Component
public class TestLuckyChanceExecute implements IApiExecute {
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //todo:wxq
        //        Assert.isTrue(ProjectTools.findMaxWinGoodNum() > 10, "测试专用~~");
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = jsonObjectAdmjson.getString("buyerNick");
        Assert.hasLength(buyerNick, "buyerNick不能为空");

        luckyDrawHelper.sendCard(buyerNick, LuckyChanceFromEnum.FREE, 100,
                "测试专用，获得【有料品鉴官】一博送你的食力拼图*" + 100);
        return YunReturnValue.ok("测试专用，获得【有料品鉴官】一博送你的食力拼图*100");
    }
}
