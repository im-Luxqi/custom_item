package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysGeneralTask;
import com.duomai.project.product.general.enums.TaskTypeEnum;

import java.util.Date;
import java.util.List;

public interface SysGeneralTaskRepository extends BaseRepository<SysGeneralTask, String> {

    //根据粉丝昵称、任务类型查询集合
    List<SysGeneralTask> findByBuyerNickAndTaskType(String buyerNick, TaskTypeEnum taskType);

    //根据粉丝昵称任务类型 count
    long countByBuyerNickAndTaskType(String buyerNick, TaskTypeEnum taskType);

    //根据粉丝昵称、任务类型、时间段查找数据
    List<SysGeneralTask> findByBuyerNickAndTaskTypeAndCreateTimeBetween(String buyerNick, TaskTypeEnum taskType,
                                                                        Date start, Date end);

    //根据粉丝昵称
    List<SysGeneralTask> findByBuyerNick(String buyerNick);

    //根据粉丝昵称清除数据
    void deleteByBuyerNick(String buyerNick);

}
