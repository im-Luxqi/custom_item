package com.duomai.new_custom_base.api.product.service.impl;

import com.duomai.new_custom_base.api.product.entity.PagePvLog;
import com.duomai.new_custom_base.api.product.repository.PagePvLogRepository;
import com.duomai.new_custom_base.api.product.service.PagePvLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @description
 * @create by 王星齐
 * @time 2020-05-27 19:22:53
 **/
@Service
public class PagePvLogServiceImpl implements PagePvLogService {
    @Autowired
    private PagePvLogRepository pagePvLogRepository;

    @Override
    public List<PagePvLog> findByBuyerNick(String buyerNick) {
        return pagePvLogRepository.findByBuyerNick(buyerNick);
    }
}
