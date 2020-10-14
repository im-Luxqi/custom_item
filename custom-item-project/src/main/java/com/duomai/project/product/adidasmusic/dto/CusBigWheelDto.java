package com.duomai.project.product.adidasmusic.dto;

import com.duomai.project.product.general.enums.CusBigWheelStateEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;

@Setter
@Getter
public class CusBigWheelDto {
    /**
     * 标题
     */
    private String title;
    /**
     * 详细
     */
    private String context;
    /**
     * 图片
     */
    private String img;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 链接
     */
    private String flyLink;

    /**
     * 状态
     * */
    private CusBigWheelStateEnum state;
}
