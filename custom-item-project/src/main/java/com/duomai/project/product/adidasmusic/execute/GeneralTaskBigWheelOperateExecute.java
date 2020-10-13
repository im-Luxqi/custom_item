package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.domain.CusBigWheelLog;
import com.duomai.project.product.adidasmusic.service.ICusBigWheelLogService;
import com.duomai.project.product.general.dto.TaskBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import com.duomai.project.product.general.enums.PvPageEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @内容：任务页面 尖货大咖操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 * */
@Component
public class GeneralTaskBigWheelOperateExecute implements IApiExecute {
    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Autowired
    private ICusBigWheelLogService iCusBigWheelLogService;
    @Autowired
    private SysLuckyChanceRepository sysLuckyChanceRepository;
    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //预防连点
        projectHelper.checkoutMultipleCommit(sysParm,this);

        Date now = sysParm.getRequestStartTime();
        // 获取尖货大咖任务开放时间
//        TaskBaseSettingDto taskSeeting = projectHelper.taskBaseSettingFind();
//        Assert.notNull(taskSeeting, "不存在相关配置");
        // 校验时间
//        Date start = CommonDateParseUtil.getStartTimeOfDay(taskSeeting.getTaskStartTime());
//        Date end = CommonDateParseUtil.getEndTimeOfDay(taskSeeting.getTaskEndTime());
//        if (now.before(start) || now.after(end)){
//            return YunReturnValue.fail("任务无法解锁");
//        }
        Date date = sysParm.getRequestStartTime();
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        // 校验玩家是否存在
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(sysCustom, "不存在该玩家");
        // 校验
        List<CusBigWheelLog> bigWheelLog = iCusBigWheelLogService.query()
                .eq(CusBigWheelLog::getBuyerNick, buyerNick)
                .between(CusBigWheelLog::getCreateTime,CommonDateParseUtil.getStartTimeOfDay(date), CommonDateParseUtil.getEndTimeOfDay(date))
                .list();
        if (bigWheelLog.size() > 0){
            Assert.isNull(bigWheelLog, "操作已完成！");
        }
        /*保存操作日志*/
        CusBigWheelLog cusBigWheelLog = new CusBigWheelLog();
        cusBigWheelLog.setBuyerNick(buyerNick);
        cusBigWheelLog.setCreateTime(now);
        cusBigWheelLog.setGateway(PvPageEnum.PAGE_DAKA.getValue());
        iCusBigWheelLogService.save(cusBigWheelLog);
        /*插入一条抽奖机会来源*/
        SysLuckyChance luckyChance = new SysLuckyChance();
        sysLuckyChanceRepository.save(luckyChance.setBuyerNick(buyerNick)
                .setGetTime(now)
                .setChanceFrom(LuckyChanceFromEnum.DAKA)
                .setIsUse(BooleanConstant.BOOLEAN_NO));
        return YunReturnValue.ok("操作成功");
    }
}
