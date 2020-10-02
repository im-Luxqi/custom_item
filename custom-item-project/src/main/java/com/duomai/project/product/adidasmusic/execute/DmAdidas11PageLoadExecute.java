package com.duomai.project.product.adidasmusic.execute;


import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.OcsData;
import com.duomai.project.api.taobao.OcsUtil;
import com.duomai.project.helper.FinishTheTaskHelper;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysInviteLog;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.CommonExceptionEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysInviteLogRepository;
import com.duomai.project.product.general.repository.SysLuckyDrawRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 活动load
 * @date 2020-10-02
 */
@Service
public class DmAdidas11PageLoadExecute implements IApiExecute {

    @Resource
    private ProjectHelper projectHelper;
    @Resource
    private LuckyDrawHelper drawHelper;
    @Resource
    private FinishTheTaskHelper taskHelper;
    @Resource
    private SysCustomRepository customRepository;
    @Resource
    private SysInviteLogRepository inviteLogRepository;
    @Resource
    private SysLuckyDrawRecordRepository drawRecordRepository;

    private static Logger logger = LoggerFactory.getLogger(DmAdidas11PageLoadExecute.class);

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        //校验活动是否在活动时间内
        ActBaseSettingDto actBaseSettingDto;
        try {
            actBaseSettingDto = projectHelper.actBaseSettingFind();
        } catch (Exception e) {
            e.printStackTrace();
            return YunReturnValue.fail(CommonExceptionEnum.ACT_BASE_SETTING_FIND_ERROR.getMsg());
        }
        projectHelper.actTimeValidate(actBaseSettingDto);

        //取参
        JSONObject object = JSONObject.parseObject(sysParm.getApiParameter().getAdmjson().toString());
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Assert.hasLength(buyerNick, CommonExceptionEnum.BUYER_NICK_ERROR.getMsg());
        //被邀请人昵称
        String inviteeNick = object.getString("inviteeNick");

        //预防并发
        try {
            projectHelper.checkoutMultipleCommit(sysParm, this::ApiExecute);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("redis error:" + e.getMessage());
            return YunReturnValue.fail(CommonExceptionEnum.SYSTEM_ERROR.getMsg());
        }

        //获取粉丝信息
        SysCustom sysCustom;
        try {
            sysCustom = customRepository.findByBuyerNick(buyerNick);
        } catch (Exception e) {
            e.printStackTrace();
            return YunReturnValue.fail(CommonExceptionEnum.FIND_BY_BUYER_NICK_ERROR.getMsg());
        }

        //为空初始化粉丝数据
        if (sysCustom == null) {
            sysCustom = projectHelper.customInit(sysParm);
            customRepository.save(sysCustom);

            //todo 是否送抽奖次数

        }

        //查询该粉丝是否被人邀请过
        long inviteLogNum = 0L;
        try {
            inviteLogNum = inviteLogRepository.countByInviter(buyerNick);
        } catch (Exception e) {
            e.printStackTrace();
            return YunReturnValue.fail(CommonExceptionEnum.COUNT_BY_INVITER_ERROR.getMsg());
        }

        //为空记录邀请日志
        List<SysInviteLog> inviteLogs;
        if (inviteLogNum == 0) {
            SysInviteLog inviteLog = new SysInviteLog();
            inviteLogRepository.save(inviteLog.setCreateTime(new Date())
                    .setInvitee(buyerNick)
                    .setInviter(inviteeNick)
            );

            //查询出当前粉丝的邀请记录
            SysInviteLog log = new SysInviteLog();
            try {
                inviteLogs = inviteLogRepository.findAll(Example.of(log.setInviter(buyerNick)));
            }catch (Exception e){
                e.printStackTrace();
                return YunReturnValue.fail(CommonExceptionEnum.GET_INVITER_LOG_ERROR.getMsg());
            }
        } else {
            return YunReturnValue.fail(CommonExceptionEnum.HELPED_INVITEE_ERROR.getMsg());
        }

        //返回参数
        LinkedHashMap linkedHashMap = new LinkedHashMap();

        //活动基本信息
        linkedHashMap.put("actBaseSettingDto", actBaseSettingDto);
        //粉丝信息
        linkedHashMap.put("sysCustom", sysCustom);
        //邀请日志记录
        linkedHashMap.put("inviteLogs", inviteLogs);

        //查询剩余抽奖次数
        long drawNum = 0L;
        try {
            drawNum = drawHelper.unUseLuckyChance(buyerNick);
        } catch (Exception e) {
            e.printStackTrace();
            return YunReturnValue.fail(CommonExceptionEnum.UN_USE_LUCKY_CHANCE_ERROR.getMsg());
        }
        //获取目前剩余抽奖次数
        linkedHashMap.put("drawNum", drawNum);

        long signNum = 0L;
        try {
            signNum = taskHelper.getFinishTheTaskNum(buyerNick);
        } catch (Exception e) {
            e.printStackTrace();
            return YunReturnValue.fail(CommonExceptionEnum.get_Finish_The_Task_Num_error.getMsg());
        }
        //获取目前签到次数
        linkedHashMap.put("signNum", signNum);

        //中奖弹幕 展示50条
        List<SysLuckyDrawRecord> luckyDrawRecords;
        try {
            luckyDrawRecords = drawRecordRepository.queryLuckyDrawLog();
        }catch (Exception e){
            e.printStackTrace();
            return YunReturnValue.fail(CommonExceptionEnum.QUERY_LUCKY_DRAW_LOG_ERROR.getMsg());
        }
        //如果没有50条数据，造假数据填补
        if(luckyDrawRecords.size() < 50){
            
        }


        return YunReturnValue.ok(linkedHashMap, CommonExceptionEnum.OPERATION_SUCCESS.getMsg());
    }
}
