package com.duomai.project.api.taobao;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description
 * @create by 王星齐
 * @date 2020-08-24 14:39
 */
@Component
public class OcsTemplate {

    @Autowired
    private MemcachedClient memcachedClient;



}
