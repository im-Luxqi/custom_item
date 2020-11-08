package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysInviteLog;
import com.duomai.project.product.general.enums.CommonExceptionEnum;
import com.duomai.project.product.general.enums.InvitationTypeEnum;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author cjw
 * @description 邀请入会
 * @time 2020-10-10
 */
@Service
public class DmInviteToJoinExecute implements IApiExecute {

    @Resource
    private SysInviteLogRepository inviteLogRepository;
    @Resource
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        /*预防并发，校验活动是否在活动时间内*/

        //是否在活动期间
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
         projectHelper.actTimeValidate();

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        buyerNick = buyerNick.replaceAll(" ","+");
        Date date = sysParm.getRequestStartTime();
        String headImg = object.getString("headImg");
        String inviterNick = object.getString("inviterNick");
        Assert.hasLength(inviterNick, "邀请人昵称不能为空!");
        inviterNick = inviterNick.replaceAll(" ","+");

        //check
        if (inviterNick.equals(buyerNick)) {
            return YunReturnValue.fail("亲、自己无法邀请自己哦!");
        }

        //查询该粉丝是否被人邀请过
        long inviteLogNum = inviteLogRepository.countByInviteeAndInvitationType(buyerNick,InvitationTypeEnum.invitationStage);
        if (inviteLogNum == 0) {//为空记录邀请日志
            SysInviteLog inviteLog = new SysInviteLog();
            inviteLogRepository.save(inviteLog.setCreateTime(date)
                    .setInvitee(buyerNick)
                    .setMixInvitee(sysParm.getApiParameter().getYunTokenParameter().getUserNick())
                    .setInviteeImg(headImg)
                    .setInviter(inviterNick)
                    .setInvitationType(InvitationTypeEnum.invitationStage)
            );
        } else {
            return YunReturnValue.fail(CommonExceptionEnum.HELPED_INVITEE_ERROR.getMsg());
        }
        return YunReturnValue.ok("操作成功");
    }
}
