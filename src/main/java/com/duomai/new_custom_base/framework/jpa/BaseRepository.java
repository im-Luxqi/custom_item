package com.duomai.new_custom_base.framework.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/*
 * @description
 * @create by 王星齐
 * @time 2020-05-27 19:21:42
 **/
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
//    List<Object[]> listBySQL(String sql);
}
