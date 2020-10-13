package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.product.general.entity.SysInviteLog;
import com.duomai.project.product.general.enums.InvitationTypeEnum;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cjw
 * @description 判断当前粉丝是否入会
 * @time 2020-10-10
 */
@Service
public class DmMembershipExecute implements IApiExecute {

    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Resource
    private SysInviteLogRepository inviteLogRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws ApiException {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        String inviterNick = object.getString("inviterNick");
        Assert.hasLength(inviterNick,"邀请人昵称不能为空!");

        if (inviterNick.equals(buyerNick)) {
            return YunReturnValue.fail("亲、自己无法邀请自己哦!");
        }

        //保存邀请日志
        SysInviteLog inviteLog = new SysInviteLog();
        inviteLogRepository.save(
                inviteLog.setInvitee(buyerNick)
                .setInviter(inviterNick)
                .setCreateTime(sysParm.getRequestStartTime())
                .setInvitationType(InvitationTypeEnum.memberStage)
        );

        return YunReturnValue.ok(taobaoAPIService.isMember(buyerNick),"操作成功");
    }
}
