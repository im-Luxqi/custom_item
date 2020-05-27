package com.duomai.new_custom_base.api.product.service;

import com.duomai.new_custom_base.api.product.entity.PagePvLog;

import java.util.List;

/*
 * @description
 * @create by 王星齐
 * @time 2020-05-27 19:22:44
 **/
public interface PagePvLogService {
    List<PagePvLog> findByBuyerNick(String buyerNick);
}
