package com.duomai.project.helper;

import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysGeneralTaskRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author cjw
 * @description 活动任务相关工具类
 * @date 2020-10-02
 */
@Component
public class FinishTheTaskHelper {

    @Resource
    private SysGeneralTaskRepository sysGeneralTaskRepository;

    //获取目前粉丝签到的次数
    public long getFinishTheTaskNum(String buyerNick){
        return sysGeneralTaskRepository.countByBuyerNickAndTaskType(buyerNick,TaskTypeEnum.SIGN);
    }


}
