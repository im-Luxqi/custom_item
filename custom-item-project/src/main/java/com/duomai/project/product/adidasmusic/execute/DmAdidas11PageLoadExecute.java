package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.util.CommonHanZiUtil;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGeneralTask;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.*;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 活动load
 * @time 2020-10-02
 */
@Service
public class DmAdidas11PageLoadExecute implements IApiExecute {

    @Resource
    private ProjectHelper projectHelper;
    @Resource
    private LuckyDrawHelper drawHelper;
    @Resource
    private SysCustomRepository customRepository;
    @Resource
    private SysInviteLogRepository inviteLogRepository;
    @Resource
    private SysLuckyDrawRecordRepository drawRecordRepository;
    @Resource
    private SysAwardRepository awardRepository;
    @Resource
    private SysGeneralTaskRepository sysGeneralTaskRepository;
    @Resource
    private ITaobaoAPIService taobaoAPIService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        /*预防并发，校验活动是否在活动时间内*/
        projectHelper.checkoutMultipleCommit(sysParm, this);
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate(actBaseSettingDto);

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();

        Boolean firstEntry = false;
        /*初始化新粉丝，粉丝每日首次登陆赠送一次抽奖机会*/
        SysCustom sysCustom = customRepository.findByBuyerNick(buyerNick);
        if (sysCustom == null) {
            //保存粉丝信息
            sysCustom = customRepository.save(projectHelper.customInit(sysParm));
            //赠送一次每日抽奖机会
            drawHelper.sendLuckyChance(sysCustom.getBuyerNick(), LuckyChanceFromEnum.FIRST, 1);
            //是第一次进活动
            firstEntry = true;
        } else {
            //查询是否赠送过
            long l = drawHelper.countTodayLuckyChanceFrom(sysCustom.getBuyerNick(), LuckyChanceFromEnum.FIRST);
            if (l == 0) {
                //赠送一次每日抽奖机会
                drawHelper.sendLuckyChance(sysCustom.getBuyerNick(), LuckyChanceFromEnum.FIRST, 1);
            }
        }

        // --------------------------------- 自动签到 ----------------------------------- //
        // 校验
        List<SysGeneralTask> signLog = sysGeneralTaskRepository.findByBuyerNickAndTaskTypeAndCreateTimeBetween(buyerNick,
                TaskTypeEnum.SIGN, CommonDateParseUtil.getStartTimeOfDay(sysParm.getRequestStartTime()),
                CommonDateParseUtil.getEndTimeOfDay(sysParm.getRequestStartTime()));
        if (signLog.isEmpty()){
            /*保存操作日志*/
            SysGeneralTask signOpt = new SysGeneralTask();
            sysGeneralTaskRepository.save(signOpt.setBuyerNick(buyerNick)
                    .setCreateTime(sysParm.getRequestStartTime())
                    .setTaskType(TaskTypeEnum.SIGN));
            /*插入一条抽奖机会来源*/
            drawHelper.sendLuckyChance(buyerNick, LuckyChanceFromEnum.SIGN, 1);
        }
        // --------------------------------- 自动签到 ----------------------------------- //

        //获取邀请奖品信息
        SysAward awardInvite = awardRepository.queryFirstByUseWay(AwardUseWayEnum.INVITE);
        Assert.notNull(awardInvite, "未获取到邀请奖品信息!");
        //获取该粉丝是否已获得日志
        SysLuckyDrawRecord drawRecord = drawRecordRepository.findFirstByPlayerBuyerNickAndAwardId(sysCustom.getBuyerNick(),
                awardInvite.getId());
        if (drawRecord != null) {
            awardInvite.setLogId(drawRecord.getId());
        } else {
            awardInvite.setLogId("");
        }

        //返回参数
        LinkedHashMap linkedHashMap = new LinkedHashMap();

        linkedHashMap.put("firstEntry",firstEntry);
        //活动基本信息
        linkedHashMap.put("actBaseSettingDto", actBaseSettingDto);
        //粉丝信息
        linkedHashMap.put("sysCustom", sysCustom);
        //邀请日志记录
        linkedHashMap.put("inviteLogs", inviteLogRepository.findByInviterAndInvitationTypeOrderByCreateTimeDesc(sysCustom.getBuyerNick(),
                InvitationTypeEnum.invitationStage));
        //邀请奖品信息
        linkedHashMap.put("awardInvite", awardInvite);
        //获取目前剩余抽奖次数
        linkedHashMap.put("drawNum", drawHelper.unUseLuckyChance(sysCustom.getBuyerNick()));
        //获取当前粉丝奖池等级
        linkedHashMap.put("signDto", drawHelper.findCurrentPoolLevel(sysCustom));
        //当前粉丝是否入会
        linkedHashMap.put("member",taobaoAPIService.isMember(buyerNick));

        //中奖弹幕 展示50条
        List luckyDrawRecords = drawRecordRepository.queryLuckyDrawLog();
        //如果没有50条数据，造假数据填补
        if (luckyDrawRecords.size() < 50) {
            for (int i = 0; i < 50 - luckyDrawRecords.size(); i++) {
                Map map = new HashMap();
                map.put("awardName", "满减优惠券");//奖品默认名称
                map.put("playerBuyerNick", CommonHanZiUtil.randomGetUnicodeHanZi() + "***");//粉丝昵称
                luckyDrawRecords.add(map);
            }
        }
        linkedHashMap.put("luckyDrawRecords", luckyDrawRecords);

        return YunReturnValue.ok(linkedHashMap, CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }

}
