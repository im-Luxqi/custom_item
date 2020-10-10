package com.duomai.project.product.adidasmusic.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 订单表 cus_order_info
 *
 * @author system
 */
@Setter
@Getter
public class CusOrderInfo {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 商品名称
	 */
	private String title;
	/**
	 * 商品图片
	 */
	private String picUrl;
	/**
	 * 商品id
	 */
	private Long numId;
	/**
	 * 
	 */
	private String orderId;
	/**
	 * 订单价格
	 */
	private String orderPrice;
	/**
	 * 
	 */
	private Date orderTime;
	/**
	 * 订单Id
	 */
	private String tid;
	/**
	 * 
	 */
	private String buyerNick;

}