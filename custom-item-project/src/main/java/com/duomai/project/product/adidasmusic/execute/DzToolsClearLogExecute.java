package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.adidasmusic.domain.CusBigWheelLog;
import com.duomai.project.product.adidasmusic.domain.CusOrderInfo;
import com.duomai.project.product.adidasmusic.service.ICusBigWheelLogService;
import com.duomai.project.product.adidasmusic.service.ICusOrderInfoService;
import com.duomai.project.product.general.entity.*;
import com.duomai.project.product.general.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 清除记录
 * */
@Component
public class DzToolsClearLogExecute implements IApiExecute {

    @Autowired
    private SysGeneralTaskRepository sysGeneralTaskRepository;

    @Autowired
    private ICusBigWheelLogService iCusBigWheelLogService;

    @Autowired
    private SysBrowseLogRepository sysBrowseLogRepository;

    @Autowired
    private SysInviteLogRepository sysInviteLogRepository;

    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;

    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;

    @Autowired
    private ICusOrderInfoService iCusOrderInfoService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

//        String buyerNick =sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = object.getString("buyerNick");

        // 签到、关注、入会记录
        List<SysGeneralTask> sysGeneralTasks = sysGeneralTaskRepository.findByBuyerNick(buyerNick);
        if (!sysGeneralTasks.isEmpty())
            sysGeneralTaskRepository.deleteInBatch(sysGeneralTasks);
        // 尖货大咖操作记录
        List<CusBigWheelLog> cusBigWheelLogs = iCusBigWheelLogService.query().eq(CusBigWheelLog::getBuyerNick, buyerNick).list();
        if (!cusBigWheelLogs.isEmpty()){
            for (CusBigWheelLog c : cusBigWheelLogs){
                iCusBigWheelLogService.removeById(c.getId());
            }
        }
        // 浏览商品记录
        List<SysBrowseLog> sysBrowseLogs = sysBrowseLogRepository.findByBuyerNick(buyerNick);
        if (!sysBrowseLogs.isEmpty())
            sysBrowseLogRepository.deleteInBatch(sysBrowseLogs);
        // 邀请记录
        List<SysInviteLog> sysInviterLogs = sysInviteLogRepository.findByInviter(buyerNick);
        if (!sysInviterLogs.isEmpty())
            sysInviteLogRepository.deleteInBatch(sysInviterLogs);
        SysInviteLog inviteLog = sysInviteLogRepository.queryFirstByInvitee(buyerNick);
        if (inviteLog != null)
            sysInviteLogRepository.delete(inviteLog);
        // 抽奖机会来源
        List<SysLuckyChance> sysLuckyChances = sysLuckyChanceRepository.findByBuyerNick(buyerNick);
        if (!sysLuckyChances.isEmpty())
            sysLuckyChanceRepository.deleteInBatch(sysLuckyChances);
        // 抽奖日志
        List<SysLuckyDrawRecord> sysLuckyDrawRecords = sysLuckyDrawRecordRepository.findByPlayerBuyerNick(buyerNick);
        if (!sysLuckyDrawRecords.isEmpty())
            sysLuckyDrawRecordRepository.deleteInBatch(sysLuckyDrawRecords);
        // pv统计
        List<SysPagePvLog> sysPagePvLogs = sysPagePvLogRepository.findByBuyerNick(buyerNick);
        if (!sysPagePvLogs.isEmpty())
            sysPagePvLogRepository.deleteInBatch(sysPagePvLogs);
        // 订单记录
        List<CusOrderInfo> cusOrderInfos = iCusOrderInfoService.query().eq(CusOrderInfo::getBuyerNick, buyerNick).list();
        if (!cusOrderInfos.isEmpty()){
            for (CusOrderInfo o : cusOrderInfos){
                iCusOrderInfoService.removeById(o.getId());
            }
        }
        return YunReturnValue.ok("操作成功！");
    }
}
