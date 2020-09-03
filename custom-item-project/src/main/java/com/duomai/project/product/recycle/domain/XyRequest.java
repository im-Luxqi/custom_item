package com.duomai.project.product.recycle.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 咸鱼数据第三方备案表 xy_request
 *
 * @author system
 */
@Setter
@Getter
@Accessors(chain = true)
public class XyRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 买家
     */
    private String buyerNick;
    /**
     * 请求的时间
     */
    private Date requestTime;
    /**
     * 请求的数据
     */
    private String requestData;
    /**
     * 响应的数据
     */
    private String responseData;
    /**
     * 是否成功
     */
    private Integer successs;

}
