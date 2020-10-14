package com.duomai.project.product.adidasmusic.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.duomai.project.product.general.enums.CusBigWheelStateEnum;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import java.util.Date;

/**
 * 【请填写功能名称】表 cus_big_wheel
 *
 * @author system
 */
@Setter
@Getter
public class CusBigWheel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.UUID)
	private String id;
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
	 * 别名
	 */
	private String alias;

}
