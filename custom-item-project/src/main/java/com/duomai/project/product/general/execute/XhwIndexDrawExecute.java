package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.XhwHelper;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.entity.XhwCustom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 新华网首页load
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 */
@Component
public class XhwIndexDrawExecute implements IApiExecute {
    @Resource
    private XhwHelper xhwHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        String ip = sysParm.getApiParameter().getCommomParameter().getIp();

        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String runing_award_id = jsonObjectAdmjson.getString("runing_award_id");
        Assert.notNull(runing_award_id, "奖品不能为空");
        //查询粉丝信息
        //todo:压测
        XhwCustom custom = xhwHelper.findCustom(buyerNick);
        XhwAward draw = xhwHelper.draw(runing_award_id, custom, sysParm.getRequestStartTime(), ip);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        if (StringUtils.isBlank(draw.getLogId())) {
            resultMap.put("win", false);
        } else {
            resultMap.put("win", true);
            resultMap.put("win_award", draw);
            draw.setDrawStartTime(null);
            draw.setAwardRunningType(null);
            draw.setLevel(null);
            draw.setSendNum(null);
            draw.setShowNum(null);
            draw.setTotalNum(null);
        }
        return YunReturnValue.ok(resultMap, "首页load," +
                "【提示：】" +
                "win = false  ---> 已抢完" +
                "win_award ---> 奖品信息"
        );
    }
}