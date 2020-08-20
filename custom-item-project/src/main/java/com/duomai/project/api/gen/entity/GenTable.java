package com.duomai.project.api.gen.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 业务表 gen_table
 */
@Data
@Entity
@Table(name = "gen_table")
@SuppressWarnings("unchecked")
@org.hibernate.annotations.Table(appliesTo = "gen_table", comment = "代码自动生成临时表")
public class GenTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '表名称'")
    private String tableName;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '表描述'")
    private String tableComment;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '实体类名称(首字母大写)'")
    private String className;
    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '生成包路径'")
    private String packageName;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '生成模块名'")
    private String moduleName;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '生成业务名'")
    private String businessName;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '生成功能名'")
    private String functionName;

    /**
     * 主键信息
     */
    @Transient
    private GenTableColumn pkColumn;

    /**
     * 表列信息
     */
    @Transient
    private List<GenTableColumn> columns;
}