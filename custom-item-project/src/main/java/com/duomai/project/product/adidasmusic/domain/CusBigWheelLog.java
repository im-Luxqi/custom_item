package com.duomai.project.product.adidasmusic.domain;


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
