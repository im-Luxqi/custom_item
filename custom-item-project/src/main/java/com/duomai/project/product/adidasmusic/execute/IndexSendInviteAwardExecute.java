package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import com.duomai.project.product.general.enums.InvitationTypeEnum;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*首页发送邀请人员的奖励
 * @description
 * @create by 王星齐
 * @time 2020-07-31 10:30:29
 **/
@Component
public class IndexSendInviteAwardExecute implements IApiExecute {
    @Autowired
    private SysInviteLogRepository sysInviteLogRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private SysAwardRepository sysAwardRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        //防止连续点击
        projectHelper.checkoutMultipleCommit(sysParm,this);
        /*1.校验*/
        //是否在活动期间
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);
        //玩家是否存在
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(
                sysParm.getApiParameter().getYunTokenParameter().getBuyerNick());
        Assert.notNull(sysCustom, "不存在该玩家");
        //玩家是否已邀请了五位好友
        long has_invite_num = sysInviteLogRepository.countByInviterAndInvitationType(sysCustom.getBuyerNick(), InvitationTypeEnum.invitationStage);
        Assert.isTrue(has_invite_num > 4, "邀请5位好友才能获得奖品");

        //给完成邀请的发放邀请奖励
        SysAward awardForInvite = sysAwardRepository.queryFirstByUseWay(AwardUseWayEnum.INVITE);
        luckyDrawHelper.directSendCoupon(awardForInvite, sysCustom, sysParm.getRequestStartTime());
        return YunReturnValue.ok("完成邀请任务，领取邀请奖励");
    }
}
