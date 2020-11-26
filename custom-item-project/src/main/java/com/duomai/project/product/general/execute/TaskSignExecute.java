package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysTaskSignLog;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysTaskSignLogRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @内容：任务页面 每日打卡操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 */
@Component
public class TaskSignExecute implements IApiExecute {
    @Autowired
    private SysTaskSignLogRepository sysTaskSignLogRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ProjectHelper projectHelper;
    @Autowired
    private LuckyDrawHelper luckyDrawHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        /*1.校验*/
        //活动只能再活动期间
        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
        projectHelper.actTimeValidate();
        //是否存在玩家
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        SysCustom syscustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(syscustom, "无效的玩家");
        Assert.isTrue(BooleanConstant.BOOLEAN_YES.equals(syscustom.getHaveAuthorization()), "请先授权");
        //校验是否已签到
        Date today = sysParm.getRequestStartTime();

        SysTaskSignLog lastLog = sysTaskSignLogRepository.findFirstByBuyerNickAndSignTime(buyerNick
                ,CommonDateParseUtil.date2string(today,"yyyy-MM-dd"));
        Assert.isNull(lastLog, "今日已签到");


        /*2.签到动作*/
        Date yesterday = CommonDateParseUtil.addDay(today, -1);
        lastLog = sysTaskSignLogRepository.findFirstByBuyerNickAndSignTime(buyerNick
                ,CommonDateParseUtil.date2string(yesterday,"yyyy-MM-dd"));
        long totalSign = sysTaskSignLogRepository.countByBuyerNick(buyerNick);
        SysTaskSignLog todaySignLog = sysTaskSignLogRepository.save(new SysTaskSignLog()
                .setBuyerNick(buyerNick)
                .setCreateTime(today)
                .setSignTime(CommonDateParseUtil.date2string(today,"yyyy-MM-dd"))
                .setContinuousNum(lastLog == null ? 1 : lastLog.getContinuousNum() + 1)
                .setTotalNum((int) totalSign + 1));


        /*3完成任务，获取奖励*/
        Integer thisSignGet = 1;
        if (todaySignLog.getContinuousNum() % actBaseSettingDto.getTaskSignContinuous() == 0) {
            thisSignGet = actBaseSettingDto.getTaskSignContinuousPayment();
        }
        luckyDrawHelper.sendLuckyChance(buyerNick, LuckyChanceFromEnum.SIGN, thisSignGet,
                "签到", "今日签到,获取" + thisSignGet + "次机会");
        return YunReturnValue.ok("完成签到任务！");
    }
}
