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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @内容：任务页面load 关注是否完成
 * @创建人：lyj
 * @创建时间：2020.9.29
 */
@Component
public class GeneralTaskLoadExecute implements IApiExecute {
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

        Map<String, Object> result = new HashMap<>();
        /*1.是否关注*/
        List<SysGeneralTask> followLog = sysGeneralTaskRepository.findByBuyerNickAndTaskType(buyerNick, TaskTypeEnum.FOLLOW);
        result.put("task_follow", followLog.size() > 0);

        /*2.今日是否签到*/
        List<SysGeneralTask> signLog = sysGeneralTaskRepository.findByBuyerNickAndTaskTypeAndCreateTimeBetween(buyerNick,
                TaskTypeEnum.SIGN, CommonDateParseUtil.getStartTimeOfDay(new Date()), CommonDateParseUtil.getEndTimeOfDay(new Date()));
        result.put("task_sign", signLog.size() > 0);
        return YunReturnValue.ok(result, "签到和关注是否完成");
    }
}
