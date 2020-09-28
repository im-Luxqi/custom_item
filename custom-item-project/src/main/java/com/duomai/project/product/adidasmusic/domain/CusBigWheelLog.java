package com.duomai.project.product.adidasmusic.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 【请填写功能名称】表 cus_big_wheel_log
 *
 * @author system
 */
@Setter
@Getter
public class CusBigWheelLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.UUID)
	private String id;
	/**
	 * 混淆昵称
	 */
	private String buyerNick;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 入口
	 */
	private String gateway;

}
