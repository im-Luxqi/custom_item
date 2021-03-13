package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 群
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "xhw_group")
@org.hibernate.annotations.Table(appliesTo = "xhw_group", comment = "群")
public class XhwGroup  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;


    @Column(columnDefinition = "varchar(64) COMMENT '群名称'")
    private String title;
    @Column(columnDefinition = "varchar(255) COMMENT '入群二维码'")
    private String qrCode;

//    @Column(columnDefinition = "int(10) COMMENT '优先级'")
//    private Integer level;
//

    @Column(columnDefinition = "int(10) COMMENT '是否弃用'")
    private Integer finish;

//    @Column(columnDefinition = "int(10) COMMENT '群最大人数'")
//    private Integer maxNum;
//
//    @Column(columnDefinition = "int(10) COMMENT '剩余人数'")
//    private Integer remainNum;

}
