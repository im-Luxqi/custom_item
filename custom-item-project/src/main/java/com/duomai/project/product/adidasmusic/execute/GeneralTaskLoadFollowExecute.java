package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.TaskInfoDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGeneralTask;
import com.duomai.project.product.general.enums.TaskFinishedTypeEnum;
import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysGeneralTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @内容：任务页面load 关注是否完成
 * @创建人：lyj
 * @创建时间：2020.9.29
 * */
@Component
public class GeneralTaskLoadFollowExecute implements IApiExecute {
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
        TaskInfoDto dto = null;
        /*1.是否关注*/
        List<SysGeneralTask> follow = sysGeneralTaskRepository.findSysGeneralTaskByBuyerNickndTaskType(
                buyerNick,TaskTypeEnum.FOLLOW.getValue());
        if (!follow.isEmpty()){
            dto = getTaskInfoDto(TaskTypeEnum.FOLLOW.getValue(), TaskFinishedTypeEnum.FINISHED.getValue());
        } else {
            dto = getTaskInfoDto(TaskTypeEnum.FOLLOW.getValue(), TaskFinishedTypeEnum.FINISHED.getValue());
        }
        return YunReturnValue.ok(dto,"关注是否完成");
    }

    private TaskInfoDto getTaskInfoDto(String taskType, String finish){
        TaskInfoDto dto = new TaskInfoDto();
        dto.setTaskInfo(taskType);
        dto.setIsFinish(finish);
        return dto;
    }
}
