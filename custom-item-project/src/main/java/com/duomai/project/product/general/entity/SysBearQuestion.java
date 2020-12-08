package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 白熊题目
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_bear_question")
@org.hibernate.annotations.Table(appliesTo = "sys_bear_question", comment = "白熊题目")
public class SysBearQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(255) COMMENT '问题'")
    private String question;
    @Column(columnDefinition = "varchar(100) COMMENT '选项A'")
    private String optionA;
    @Column(columnDefinition = "varchar(100) COMMENT '选项B'")
    private String optionB;
    @Column(columnDefinition = "varchar(100) COMMENT '选项C'")
    private String optionC;
    @Column(columnDefinition = "varchar(10) COMMENT '答案'")
    private String answer;
}
