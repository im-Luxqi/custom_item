package com.duomai.project.product.general.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TaskBaseSettingDto {

    // 开始时间
    private Date taskStartTime;

    // 结束时间
    private Date taskEndTime;
}
