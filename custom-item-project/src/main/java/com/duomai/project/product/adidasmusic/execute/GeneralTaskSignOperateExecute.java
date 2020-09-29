package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGeneralTask;
import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysGeneralTaskRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @内容：任务页面 每日打卡操作
 * @创建人：lyj
 * @创建时间：2020.9.30
 * */
@Component
public class GeneralTaskSignOperateExecute implements IApiExecute {
    @Autowired
    private SysGeneralTaskRepository sysGeneralTaskRepository;
    @Autowired
    private SysCustomRepository sysCustomRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        // 校验玩家是否存在
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);
        Assert.notNull(sysCustom, "不存在该玩家");
        // 校验
        List<SysGeneralTask> signLog = sysGeneralTaskRepository.findByBuyerNickAndTaskTypeAndCreateTimeBetween(buyerNick,
                TaskTypeEnum.SIGN, CommonDateParseUtil.getStartTimeOfDay(new Date()), CommonDateParseUtil.getEndTimeOfDay(new Date()));
        if (signLog.size() > 0){
            Assert.isNull(signLog, "重复操作！");
        }
        /*保存操作日志*/
        SysGeneralTask signOpt = new SysGeneralTask();
        sysGeneralTaskRepository.save(signOpt.setBuyerNick(buyerNick)
                .setCreateTime(sysParm.getRequestStartTime())
                .setTaskType(TaskTypeEnum.SIGN));
        return YunReturnValue.ok("操作成功！");
    }
}
