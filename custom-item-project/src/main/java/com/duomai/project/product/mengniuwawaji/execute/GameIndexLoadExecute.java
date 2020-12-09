package com.duomai.project.product.mengniuwawaji.execute;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysPagePvLog;
import com.duomai.project.product.general.entity.SysTaskInviteLog;
import com.duomai.project.product.general.enums.CoachConstant;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 游戏首页 加载
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameIndexLoadExecute implements IApiExecute {

    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;

    @Autowired
    private SysSettingAwardRepository sysSettingAwardRepository;

    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;


    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;


    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
//        resultMap.put("alter_for_invitee_flag", false);


        /*1.校验是否存在玩家*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");



        /*保存pv*/
        sysPagePvLogRepository.save(new SysPagePvLog()
                .setBuyerNick(sysParm.getApiParameter().getYunTokenParameter().getBuyerNick())
                .setCreateTime(sysParm.getRequestStartTime())
                .setId(sysParm.getApiParameter().getCommomParameter().getIp()).setPage(PvPageEnum.PAGE_INDEX)
        );

        boolean actLive = projectHelper.actTimeValidateFlag();
        if (actLive) {
            /*2.被邀请助力或者被邀请入会*/
            JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
            Boolean joinMemberBack = jsonObjectAdmjson.getBoolean("joinMemberBack");
            String inviter = jsonObjectAdmjson.getString("inviter");

            String successPic = "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/modal/zhulisuccess.png";
            String errorPic = "https://cjwx.oss-cn-zhangjiakou.aliyuncs.com/front/%E8%92%99%E7%89%9B/modal/failimg2.png";

            //邀请入会
            if (StringUtils.isNotBlank(inviter)) {
                inviter = inviter.replaceAll(" ", "+");
                SysCustom inviterCustom = sysCustomRepository.findByBuyerNick(inviter);
                Assert.notNull(inviterCustom, "无效的邀请者");


                String todayString = CommonDateParseUtil.date2string(sysParm.getRequestStartTime(), "yyyy-MM-dd");
                SysTaskInviteLog sysTaskInviteLog = new SysTaskInviteLog().setCreateTime(sysParm.getRequestStartTime())
                        .setMixInvitee(syscustom.getBuyerNick())
                        .setInviteTime(todayString)
                        .setInvitee(syscustom.getZnick())
                        .setInviteeImg(syscustom.getHeadImg())
                        .setMixInviter(inviterCustom.getBuyerNick())
                        .setInviter(inviterCustom.getZnick())
                        .setInviterImg(inviterCustom.getHeadImg());

                //邀请非回调进页面
                if (joinMemberBack == null || !joinMemberBack) {
                    resultMap.put("alter_for_invitee_flag", true);
                    resultMap.put("alter_for_invitee_msg", "符合邀请助力条件，引导入会");
                    resultMap.put("alter_for_invitee_pic", errorPic);
                    resultMap.put("alter_for_inviter_img", inviterCustom.getHeadImg());
                    if (inviter.equals(buyerNick)) {
                        resultMap.put("alter_for_invitee_msg", "无法帮自己助力");
                        sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                    } else {
                        if (!MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())
                                || taobaoAPIService.isMember(buyerNick)) {
                            resultMap.put("alter_for_invitee_msg", "您不是店铺新会员，无法为好友助力");
                            sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                        }
                    }
                } else {
                    sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_YES);
                    if (inviter.equals(buyerNick) ||
                            !taobaoAPIService.isMember(buyerNick) ||
                            BooleanConstant.BOOLEAN_NO.equals(syscustom.getHaveAuthorization()) ||
                            !MemberWayFromEnum.NON_MEMBER.equals(syscustom.getMemberWayFrom())) {
                        sysTaskInviteLog.setHaveSuccess(BooleanConstant.BOOLEAN_NO);
                    }
                    //邀请成功,发放抽奖机会
                    if (BooleanConstant.BOOLEAN_YES.equals(sysTaskInviteLog.getHaveSuccess())) {
                        sysCustomRepository.save(syscustom.setMemberWayFrom(MemberWayFromEnum.INVITEE_JOIN_MEMBER));
//                        luckyDrawHelper.sendLuckyChance(inviterCustom.getBuyerNick(), LuckyChanceFromEnum.INVITE_MEMBER, 1,
//                                "邀请入会" + syscustom.getZnick(), "邀请任务，获得了游戏机会");
                        resultMap.put("alter_for_invitee_flag", true);
                        resultMap.put("alter_for_invitee_msg", "恭喜你，助力成功");
                        resultMap.put("alter_for_invitee_pic", successPic);
                        resultMap.put("alter_for_inviter_img", inviterCustom.getHeadImg());

                        if (BooleanConstant.BOOLEAN_NO.equals(syscustom.getHaveInviteFriend())) {
                            syscustom.setStarValue(syscustom.getStarValue() + CoachConstant.joinmember_xingyuan);
                            syscustom.setHaveInviteFriend(BooleanConstant.BOOLEAN_YES);
                            syscustom = sysCustomRepository.save(syscustom);
                        }
                    }
                }
                //记录邀请日志
                if (sysTaskInviteLog.getHaveSuccess() != null) {
                    sysTaskInviteLogRepository.save(sysTaskInviteLog);
                }
            }
        }

        //1.活动规则
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        actBaseSettingDto.setActLastTime(null);
        actBaseSettingDto.setOrderStartTime(null);
        actBaseSettingDto.setOrderEndTime(null);
        resultMap.put("game_rule", actBaseSettingDto);
        //2.星愿值
        resultMap.put("total_star_value", syscustom.getStarValue());
        //2.兑换弹幕
        resultMap.put("lucky_barrage", luckyDrawHelper.luckyBarrage());
        return YunReturnValue.ok(resultMap, "游戏门头");
    }
}




