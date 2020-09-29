package com.duomai.project.api.gateway;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.common.enums.SysErrorEnum;
import com.duomai.project.product.adidasmusic.execute.*;
import com.duomai.project.product.general.execute.*;
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
        map.put("wx.dz.common.member.success", MemberSuccessExecute.class); //入会成功后，完成用户信息 wxq
        map.put("wx.dz.common.init.attention", InitAttentionStatusExecute.class); //活动主页--load后，初始化玩家关注状态 wxq
        map.put("wx.dz.common.award.address", AwardAddressExecute.class); //留资料 wxq
        map.put("wx.dz.common.award.myluckybag", AwardMyLuckyBagExecute.class); //我的奖品 wxq


        /*
         * 定制
         **/
        map.put("wx.dz.index.award.forinvite", IndexSendInviteAwardExecute.class); //首页发送邀请人员的奖励 wxq
        map.put("wx.dz.index.award.luckydraw", IndexLuckyDrawExecute.class); //抽奖 wxq

        map.put("wx.dz.task.finish.load", GeneralTaskLoadExecute.class); // 任务页面load:签到、关注是否完成 lyj
        map.put("wx.dz.task.sign.operate", GeneralTaskSignOperateExecute.class); // 任务页面：完成每日打卡
        map.put("wx.dz.task.follow.operate", GeneralTaskFollowOperateExecute.class); // 任务页面：完成关注店铺
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
