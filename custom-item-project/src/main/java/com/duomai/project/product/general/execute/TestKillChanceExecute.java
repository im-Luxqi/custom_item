package com.duomai.project.product.general.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.repository.*;
import com.duomai.project.product.mengniuwawaji.domain.CusOrderInfo;
import com.duomai.project.product.mengniuwawaji.service.ICusOrderInfoService;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 杀死  比尔
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 * @time 2020-08-26 19:11:50
 */
@Component
public class TestKillChanceExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;

    @Autowired
    private SysLuckyDrawRecordRepository sysLuckyDrawRecordRepository;
    @Autowired
    private SysExchangeLogRepository sysExchangeLogRepository;
    @Autowired
    private SysTaskBrowseLogRepository sysTaskBrowseLogRepository;


    @Autowired
    private SysTaskInviteLogRepository sysTaskInviteLogRepository;


    @Autowired
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;


    @Autowired
    private SysTaskShareLogRepository sysTaskShareLogRepository;


    @Autowired
    private SysTaskSignLogRepository sysTaskSignLogRepository;


    @Autowired
    private ICusOrderInfoService cusOrderInfoService;


    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Assert.isTrue(ProjectTools.findMaxWinGoodNum() > 10, "测试专用~~");
        JSONObject jsonObjectAdmjson = sysParm.getApiParameter().findJsonObjectAdmjson();
        String buyerNick = jsonObjectAdmjson.getString("buyerNick");
        Assert.hasLength(buyerNick, "buyerNick不能为空");


        sysCustomRepository.deleteByBuyerNick(buyerNick);
        sysLuckyChanceRepository.deleteByBuyerNick(buyerNick);
        sysLuckyDrawRecordRepository.deleteByPlayerBuyerNick(buyerNick);
        sysExchangeLogRepository.deleteByBuyerNick(buyerNick);
        sysTaskBrowseLogRepository.deleteByBuyerNick(buyerNick);
        sysTaskInviteLogRepository.deleteByMixInvitee(buyerNick);
        sysTaskInviteLogRepository.deleteByMixInviter(buyerNick);
        sysTaskMemberOrFollowRepository.deleteByBuyerNick(buyerNick);
        sysTaskShareLogRepository.deleteByMixShareder(buyerNick);
        sysTaskShareLogRepository.deleteByMixSharer(buyerNick);
        sysTaskSignLogRepository.deleteByBuyerNick(buyerNick);

        cusOrderInfoService.delete().eq(CusOrderInfo::getBuyerNick, buyerNick);

        return YunReturnValue.ok("恭喜【" + buyerNick + "】被kill");
    }
}
