package com.duomai.project.api.gateway;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.enums.SysErrorEnum;
import com.duomai.project.product.general.execute.AuthorizationSuccessExecute;
import com.duomai.project.product.general.execute.InitAttentionStatusExecute;
import com.duomai.project.product.general.execute.PagePvExecute;
import com.duomai.project.product.recycle.execute.IndexLoadExecute;
import com.duomai.project.product.recycle.execute.LuckyBagLoadExecute;
import com.duomai.project.product.recycle.execute.LuckyDrawExecute;
import com.duomai.project.product.recycle.execute.LuckyDrawLoadExecute;
import com.duomai.project.tool.ApplicationUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class QLApiExecuteHandler {

    public static Map map = new HashMap<>();

    static {
        /*
         * 通用
         **/
        map.put("wx.dz.common.pv", PagePvExecute.class);//pv  wxq
        map.put("wx.dz.common.auth.success", AuthorizationSuccessExecute.class);//授权成功后，完善用户信息  wxq
        map.put("wx.dz.common.init.attention", InitAttentionStatusExecute.class); //活动主页--load后，初始化玩家关注状态 wxq


        /*
         * 定制
         **/
        map.put("wx.dz.adidas.load.index", IndexLoadExecute.class); //首页load, wxq
        map.put("wx.dz.adidas.load.luckydraw", LuckyDrawLoadExecute.class); //抽奖页load, wxq
        map.put("wx.dz.adidas.post.luckydraw", LuckyDrawExecute.class); //抽奖, wxq
        map.put("wx.dz.adidas.load.luckybag", LuckyBagLoadExecute.class); //我的奖品, wxq
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
        return sendApiExecute.ApiExecute(sysParm, request, response);
    }
}
