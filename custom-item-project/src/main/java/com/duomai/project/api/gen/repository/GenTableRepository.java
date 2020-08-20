package com.duomai.project.api.gen.repository;

import com.duomai.project.api.gen.entity.GenTable;
import com.duomai.project.framework.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenTableRepository extends BaseRepository<GenTable, Long> {
    @Query(nativeQuery = true,
            value = "SELECT table_name, table_comment " +
                    "FROM information_schema.tables " +
                    "WHERE table_name NOT LIKE 'gen_%' " +
                    "   AND table_name NOT LIKE 'qrtz_%' " +
                    "   AND table_schema = (select database()) " +
                    "   AND (table_name in (?1) or ?1 is null ) ")
    List<Object[]> selectDbTableListByNames(String[] tableNames);

    @Query(nativeQuery = true,
            value = "SELECT table_name, table_comment " +
                    "FROM information_schema.tables " +
                    "WHERE table_name NOT LIKE 'gen_%' " +
                    "   AND table_name NOT LIKE 'qrtz_%' " +
                    "   AND table_schema = (select database())  " +
                    "   AND table_name NOT IN (select table_name from gen_table) " +
                    "   AND (table_name like concat('%', ?1, '%') or ?1 is null )" +
                    "   AND (table_comment like concat('%', ?2, '%') or ?2 is null ) ")
    Page<Object[]> selectDbTableList(String tableName, String tableComment, PageRequest of);

    @Query(nativeQuery = true,
            value = "SELECT column_name, column_comment, column_type " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = (select database()) " +
                    "   AND (table_name = ?1 or ?1 is null ) " +
                    "ORDER BY ordinal_position")
    List<Object[]> selectDbTableColumnsByName(String tableName);
}
