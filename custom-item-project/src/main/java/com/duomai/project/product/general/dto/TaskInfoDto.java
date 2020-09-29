package com.duomai.project.product.general.dto;

import lombok.Data;

/**
 * 任务信息
 * */
@Data
public class TaskInfoDto {
    // 任务信息
    private String taskInfo;

    // 任务完成情况
    private String isFinish;
}
