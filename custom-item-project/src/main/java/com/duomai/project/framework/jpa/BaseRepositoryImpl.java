package com.duomai.project.framework.jpa;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

/*
 * @description
 * @create by 王星齐
 * @time 2020-05-27 19:20:57
 **/
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    //通过EntityManager来完成查询
//    @Override
//    public List<Object[]> listBySQL(String sql) {
//        return entityManager.createNativeQuery(sql).getResultList();
//    }

}
