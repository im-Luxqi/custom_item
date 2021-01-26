package com.duomai.project.api.gateway;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.enums.SysErrorEnum;
import com.duomai.project.api.taobao.MemcacheTools;
import com.duomai.project.product.general.execute.*;
import com.duomai.project.tool.ApplicationUtils;
import com.duomai.project.tool.ProjectTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class QLApiExecuteHandler {

    public static Map map = new HashMap<>();

    static {
        /**
         * 通用
         */
        /*前台*/
        //首页load
        map.put("wx.dz.xhw.index.load", XhwIndexLoadExecute.class);
        //马上抢
        map.put("wx.dz.xhw.index.draw", XhwIndexDrawExecute.class);
        //我的礼品
        map.put("wx.dz.xhw.luckybag.list", XhwLuckyBagAllWinExecute.class);
        //填写信息
        map.put("wx.dz.xhw.luckybag.address", XhwLuckyBagFillAwardAddressExecute.class);
        //刷新running award
        map.put("wx.dz.xhw.refresh.award", XhwRefreshAwardExecute.class);
        /*后台*/
        //拜年贴  新增,删除,修改
        //奖品  新增,删除,修改
        //群  新增
        //虚拟人数  修改

    }


    public static YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApplicationContext applicationContext = ApplicationUtils.getContext();
        IApiExecute sendApiExecute = null;
        for (Object o : map.keySet()) {
            if (o.equals(sysParm.getMethod())) {
                sendApiExecute = applicationContext.getBean((Class<IApiExecute>) map.get(o.toString()));
                break;
            }
        }
        if (sendApiExecute == null) {
            return YunReturnValue.fail(SysErrorEnum.VALID_EXECUTE);
        }

        //防连点
        if (ProjectTools.hasMemCacheEnvironment()) {
            if (StringUtils.isNotBlank(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())) {
                Assert.isTrue(MemcacheTools.add("_checkoutMultipleCommit_"
                        + sysParm.getApiParameter().getYunTokenParameter().getBuyerNick()
                        + sendApiExecute.getClass().getName()), "点太快了，请休息下");
            }
        }
        return sendApiExecute.ApiExecute(sysParm, request, response);
    }
}
