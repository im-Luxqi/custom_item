package com.duomai.new_custom_base.api.product.core;

import com.duomai.new_custom_base.api.product.core.apiReturnValue.YunReturnValue;
import com.duomai.new_custom_base.api.product.core.pojo.ApiSysParameter;
import com.duomai.new_custom_base.api.product.core.service.IApiExecute;
import com.duomai.new_custom_base.common.tool.ApplicationUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class QLApiExecuteHandler {

    public static Map map = new HashMap<>();

    static {
        /*
         * adidas-zx
         **/
//        map.put("wx.adidas.game.index", GameIndexLoadExecute.class);//首页load  wxq
//        map.put("wx.adidas.auth.success", AuthorizationSuccessExecute.class);//授权成功后，完善用户信息  wxq
//        map.put("wx.adidas.draw.post", DrawPostExecute.class);//翻拍抽奖  wxq
//        map.put("wx.adidas.my.honor", MyHonorExecute.class);//我抽中的奖品  wxq
//        map.put("wx.adidas.complete.address", CompleteAddressExecute.class);//完善地址信息  wxq
//        map.put("wx.adidas.my.task", MyTaskExecute.class);//任务完成情况  wxq
//        map.put("wx.adidas.do.task", DoTaskExecute.class);//做任务赢次数  wxq
//        map.put("wx.adidas.page.pv", AdPagePvExcute.class);//pv  wxq
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
            throw new Exception("sendApiExecute 为空，可能没有配置api接口");
        }
        return sendApiExecute.ApiExecute(sysParm, request, response);
    }
}
