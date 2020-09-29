package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysGeneralTask;

import java.util.Date;
import java.util.List;

public interface SysGeneralTaskRepository extends BaseRepository<SysGeneralTask, String> {

    List<SysGeneralTask> findSysGeneralTaskByBuyerNickndTaskType(String buyerNick, String taskType);
}
