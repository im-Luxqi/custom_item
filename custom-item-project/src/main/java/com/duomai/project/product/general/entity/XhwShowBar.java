package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 拜年帖
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "xhw_show_bar")
@org.hibernate.annotations.Table(appliesTo = "xhw_show_bar", comment = "拜年帖")
public class XhwShowBar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;

    @Column(nullable = false, columnDefinition = "varchar(40) COMMENT '标题'")
    private String title;
    @Column(columnDefinition = "varchar(255) COMMENT '图片'")
    private String img;
    @Column(columnDefinition = "text COMMENT '描述'")
    private String description;

    @Column(columnDefinition = "int(10) COMMENT '优先级'")
    private Integer level;

    @Column(columnDefinition = "varchar(255) COMMENT '市长'")
    private String leader;

}
