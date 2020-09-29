package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysGeneralTask;
import com.duomai.project.product.general.enums.TaskTypeEnum;

import java.util.Date;
import java.util.List;

public interface SysGeneralTaskRepository extends BaseRepository<SysGeneralTask, String> {

    List<SysGeneralTask> findByBuyerNickAndTaskType(String buyerNick, TaskTypeEnum taskType);

    List<SysGeneralTask> findByBuyerNickAndTaskTypeAndCreateTimeBetweenAnd(String buyerNick, TaskTypeEnum taskType,
                                                                           Date start, Date end);

}
