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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 清除记录
 * */
@Component
public class DzToolsClearLogExecute implements IApiExecute {

    @Autowired
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;
    @Autowired
    private ICusBigWheelLogService iCusBigWheelLogService;
    @Autowired
    private SysTaskBrowseLogRepository sysTaskBrowseLogRepository;
    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;
    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysPagePvLogRepository sysPagePvLogRepository;
    @Autowired
    private ICusOrderInfoService iCusOrderInfoService;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

//        String buyerNick =sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = object.getString("buyerNick");

        // 签到、关注、入会记录
        List<SysTaskMemberOrFollowLog> sysTaskMemberOrFollowLogs = sysTaskMemberOrFollowRepository.findByBuyerNick(buyerNick);
        if (!sysTaskMemberOrFollowLogs.isEmpty())
            sysTaskMemberOrFollowRepository.deleteInBatch(sysTaskMemberOrFollowLogs);
        // 尖货大咖操作记录
        List<CusBigWheelLog> cusBigWheelLogs = iCusBigWheelLogService.query().eq(CusBigWheelLog::getBuyerNick, buyerNick).list();
        if (!cusBigWheelLogs.isEmpty()){
            for (CusBigWheelLog c : cusBigWheelLogs){
                iCusBigWheelLogService.removeById(c.getId());
            }
        }
        // 浏览商品记录
        List<SysTaskBrowseLog> sysTaskBrowseLogs = sysTaskBrowseLogRepository.findByBuyerNick(buyerNick);
        if (!sysTaskBrowseLogs.isEmpty())
            sysTaskBrowseLogRepository.deleteInBatch(sysTaskBrowseLogs);
        // 邀请记录
        List<SysTaskInviteLog> sysInviterLogs = sysTaskInviteLogRepository.findByInviter(buyerNick);
        if (!sysInviterLogs.isEmpty())
            sysTaskInviteLogRepository.deleteInBatch(sysInviterLogs);
        List<SysTaskInviteLog> logs = sysTaskInviteLogRepository.findByInvitee(buyerNick);
        if (!logs.isEmpty())
            sysTaskInviteLogRepository.deleteInBatch(logs);
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
        // 粉丝数据
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);
        if (sysCustom != null)
            sysCustomRepository.delete(sysCustom);
        return YunReturnValue.ok("操作成功！");
    }
}
