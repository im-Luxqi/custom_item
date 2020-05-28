package com.duomai.new_custom_base.product;

import com.duomai.new_custom_base.api.product.entity.PagePvLog;
import com.duomai.new_custom_base.api.product.repository.PagePvLogRepository;
import com.duomai.new_custom_base.product.gen.service.IGenTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-27 17:07
 */
@RestController
public class TestController {
    @Autowired
    IGenTableService iGenTableService;
    @Autowired
    PagePvLogRepository pagePvLogRepository;

    @GetMapping("/test")
    public String test() {
        iGenTableService.getById(1);
        PagePvLog pagePvLog = new PagePvLog();
        pagePvLog.setBuyerNick("121231");
        pagePvLog.setPage("eee");
        pagePvLog.setCreateTime(new Date());
        pagePvLog.setUpdateTime(new Date());
        pagePvLogRepository.save(pagePvLog);
//        List<PagePvLog> all = pagePvLogRepository.findAll();
        return "success";
    }
}
