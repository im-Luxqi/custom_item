package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysTaskDailyBoard;
import com.duomai.project.product.general.entity.SysTaskInviteLog;
import com.duomai.project.product.general.entity.SysTaskShareLog;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.InviteTypeEnum;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskInviteLogRepository;
import com.duomai.project.product.general.repository.SysTaskShareLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 邀请助力
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-05-09 09:13:09
 */
@Component
public class TaskInviteExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private FinishTheTaskHelper finishTheTaskHelper;
    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;
    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;

    static String invite_success_img = "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/modal/zhulisuccess.png";
    static String invite_error_img = "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/modal/failimg2.png";

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.校验*/
        //活动只能再活动期间
        projectHelper.actTimeValidate();
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");

        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String inviter = jsonObjectAdmjson.getString("inviter");
        InviteTypeEnum inviteType = ProjectTools.enumValueOf(InviteTypeEnum.class, jsonObjectAdmjson.getString("inviteType"));
        Assert.notNull(inviter, "邀请人不能为空");
        Assert.notNull(inviteType, "邀请类型不能为空");
        SysCustom inviterCustom = sysCustomRepository.findByBuyerNick(inviter);
        Assert.notNull(inviterCustom, "无效的邀请人");

        String todayString = CommonDateParseUtil.date2string(new Date(), "yyyy-MM-dd");
        boolean invite_flag = true;
        InviteTypeEnum invite_type = inviteType;
        String invite_flag_img = "";
        String invite_flag_msg = "";
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        switch (inviteType) {
            case INVITE_COMMON:
                //校验分享情况
//                long inviteTime = luckyDrawHelper.countTodayLuckyChanceFrom(inviter, LuckyChanceFromEnum.SHARE);
//                long inviteTime = luckyDrawHelper.countTodayLuckyChanceFrom(inviter, LuckyChanceFromEnum.SHARE);
//                if (inviteTime >= 2) {
//                    invite_flag = false;
//                    invite_flag_img = invite_error_img;
//                    invite_flag_msg = "您今日的好友助力次数已达上限无法为好友助力，明天再来吧~";
//                    break;
//                }
                long l = sysTaskShareLogRepository.countByMixSharerAndHaveSuccessAndShareTimeAndMixShareder(inviter, BooleanConstant.BOOLEAN_YES, todayString, buyerNick);
                if (l > 0) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您已经帮TA助力，一起参与活动赢好礼吧~";
                    break;
                }
                long ll = sysTaskShareLogRepository.countByMixSharederAndHaveSuccessAndShareTime(buyerNick, BooleanConstant.BOOLEAN_YES, todayString);
                if (ll > 0) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您今日的好友助力次数已达上限，\n无法为好友助力，明天再来吧~";
                    break;
                }

                //记录分享日志
                SysTaskShareLog sysInviteLog = new SysTaskShareLog()
                        .setCreateTime(sysParm.getRequestStartTime())
                        .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                        .setShareTime(todayString)
                        .setMixShareder(syscustom.getBuyerNick())
                        .setShareder(syscustom.getZnick())
                        .setSharederImg(syscustom.getHeadImg())
                        .setMixSharer(inviterCustom.getBuyerNick())
                        .setSharer(inviterCustom.getZnick())
                        .setSharerImg(inviterCustom.getHeadImg());
                sysTaskShareLogRepository.save(sysInviteLog);

                long inviteNum = sysTaskShareLogRepository.countByMixSharerAndHaveSuccess(inviter, BooleanConstant.BOOLEAN_YES);
                SysTaskDailyBoard taskDailyBoard = finishTheTaskHelper.todayTaskBoard(inviter);
//                taskDailyBoard.setTodayFinishShareNum("(" + (inviteTime) + "/" + 2 + ")");
                Integer taskShouldInvite = actBaseSettingDto.getTaskShareShould();
                if (inviteNum % taskShouldInvite == 0) {
                    Integer thisGet = 1;
                    luckyDrawHelper.sendCard(inviter, LuckyChanceFromEnum.SHARE, thisGet,
                            "恭喜你！分享成功");
//                    taskDailyBoard.setTodayFinishShareNum("(" + (inviteTime + 1) + "/" + 2 + ")");
                }
                taskDailyBoard.setShareProgress("(" + (inviteNum % taskShouldInvite) + "/" + taskShouldInvite + ")");
                finishTheTaskHelper.updateTaskBoard(taskDailyBoard);
                invite_flag = true;
                invite_flag_img = invite_success_img;
                invite_flag_msg = "";
                break;

            case INVITE_FOLLOW:
                //校验邀请关注店铺情况
                long l1 = sysTaskInviteLogRepository.countByInviteTypeAndMixInviteeAndHaveSuccessAndMixInviter(
                        InviteTypeEnum.INVITE_FOLLOW, buyerNick, BooleanConstant.BOOLEAN_YES, inviter);
                if (l1 > 0) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您已经帮TA助力，一起参与活动赢好礼吧~";
                    break;
                }
                long l2 = sysTaskInviteLogRepository.countByInviteTypeAndMixInviteeAndHaveSuccess(
                        InviteTypeEnum.INVITE_FOLLOW, buyerNick, BooleanConstant.BOOLEAN_YES);
                if (l2 > 0) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您已经帮助其他好友助力，\n不可再次被邀请助力！";
                    break;
                }
                if (!FollowWayFromEnum.NON_FOLLOW.equals(syscustom.getFollowWayFrom())) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您不是活动期间首次关注店铺，\n无法为好友助力！";
                    break;
                }

                SysTaskInviteLog sysTaskInviteLog = new SysTaskInviteLog().setCreateTime(sysParm.getRequestStartTime())
                        .setInviteType(InviteTypeEnum.INVITE_FOLLOW)
                        .setMixInvitee(syscustom.getBuyerNick())
                        .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                        .setInviteTime(todayString)
                        .setInvitee(syscustom.getZnick())
                        .setInviteeImg(syscustom.getHeadImg())
                        .setMixInviter(inviterCustom.getBuyerNick())
                        .setInviter(inviterCustom.getZnick())
                        .setInviterImg(inviterCustom.getHeadImg());
                sysTaskInviteLogRepository.save(sysTaskInviteLog);
                long inviteFollowNum = sysTaskInviteLogRepository.countByInviteTypeAndMixInviterAndHaveSuccess(InviteTypeEnum.INVITE_FOLLOW, inviter, BooleanConstant.BOOLEAN_YES);


                Integer taskShouldInviteFollow = actBaseSettingDto.getTaskShareFollowShould();
                if (inviteFollowNum % taskShouldInviteFollow == 0) {
                    Integer thisGet = 1;
                    luckyDrawHelper.sendCard(inviter, LuckyChanceFromEnum.SHARE_FOLLOW, thisGet,
                            "恭喜你！邀请关注店铺成功");
                }
                SysTaskDailyBoard taskDailyBoard2 = finishTheTaskHelper.todayTaskBoard(inviter);
                taskDailyBoard2.setInviteFollowProgress("(" + (inviteFollowNum % taskShouldInviteFollow) + "/" + taskShouldInviteFollow + ")");
                finishTheTaskHelper.updateTaskBoard(taskDailyBoard2);
                invite_flag = true;
                invite_flag_img = invite_success_img;
                invite_flag_msg = "";
                syscustom.setFollowWayFrom(FollowWayFromEnum.INVITEE_JOIN_FOLLOW);
                sysCustomRepository.save(syscustom);
                break;


            case INVITE_MEMBER:
                //校验邀请入会情况
                long l3 = sysTaskInviteLogRepository.countByInviteTypeAndMixInviteeAndHaveSuccessAndMixInviter(
                        InviteTypeEnum.INVITE_MEMBER, buyerNick, BooleanConstant.BOOLEAN_YES, inviter);
                if (l3 > 0) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您已经帮TA助力，一起参与活动赢好礼吧~";
                    break;
                }
                long l4 = sysTaskInviteLogRepository.countByInviteTypeAndMixInviteeAndHaveSuccess(
                        InviteTypeEnum.INVITE_MEMBER, buyerNick, BooleanConstant.BOOLEAN_YES);
                if (l4 > 0) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您已经帮助其他好友助力，\n不可再次被邀请助力！";
                    break;
                }
                if (!MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())) {
                    invite_flag = false;
                    invite_flag_img = invite_error_img;
                    invite_flag_msg = "您不是店铺新会员，\n无法为好友助力！";
                    break;
                }
                SysTaskInviteLog sysTaskInviteLog2 = new SysTaskInviteLog().setCreateTime(sysParm.getRequestStartTime())
                        .setInviteType(InviteTypeEnum.INVITE_MEMBER)
                        .setMixInvitee(syscustom.getBuyerNick())
                        .setInviteTime(todayString)
                        .setInvitee(syscustom.getZnick())
                        .setInviteeImg(syscustom.getHeadImg())
                        .setMixInviter(inviterCustom.getBuyerNick())
                        .setInviter(inviterCustom.getZnick())
                        .setHaveSuccess(BooleanConstant.BOOLEAN_YES)
                        .setInviterImg(inviterCustom.getHeadImg());
                sysTaskInviteLogRepository.save(sysTaskInviteLog2);


                Integer thisGet = 1;
                luckyDrawHelper.sendCard(inviter, LuckyChanceFromEnum.SHARE_MEMBER, thisGet,
                        "恭喜你！邀请入会成功");

                invite_flag = true;
                invite_flag_img = invite_success_img;
                invite_flag_msg = "";
                syscustom.setMemberWayFrom(MemberWayFromEnum.INVITEE_JOIN_MEMBER);
                sysCustomRepository.save(syscustom);
                break;
        }

        LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
        result.put("invite_flag", invite_flag);
        result.put("invite_type", invite_type);
        result.put("invite_flag_img", invite_flag_img);
        result.put("invite_flag_msg", invite_flag_msg);
        return YunReturnValue.ok(result, "助力任务");
    }
}
