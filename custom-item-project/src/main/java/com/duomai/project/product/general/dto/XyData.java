package com.duomai.project.product.general.dto;

import com.duomai.project.tool.CommonDateParseUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @description
 * @create by 王星齐
 * @date 2020-09-02 15:53
 */
@Data
public class XyData {
    private String orderSn;
    private String state;
    private String username;
    private String tel;
    private String logTime;
    private String orderTimeYYYYMMDD;

    public void setLogTime(String logTime) {
        this.logTime = logTime;
        if (StringUtils.isNotBlank(logTime))
            this.orderTimeYYYYMMDD = CommonDateParseUtil.date2string(
                    new Date(Long.parseLong(logTime)),
                    "yyyy-MM-dd");
    }
}
