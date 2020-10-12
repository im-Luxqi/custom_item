package com.duomai.project.product;

import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.configuration.SysCustomProperties;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysCommodityRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * test
 *
 * @description
 */
@RestController
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    DataSource dataSource;

    @Autowired
    ProjectHelper projectHelper;

    @Autowired
    LuckyDrawHelper luckyDrawHelper;

    @Autowired
    DefaultTaobaoClient defaultTaobaoClient;

    @Autowired
    SysCustomProperties sysCustomProperties;

    @Autowired
    SysAwardRepository sysAwardRepository;

    @Autowired
    ITaobaoAPIService taobaoAPIService;

    @Autowired
    SysLuckyChanceRepository sysLuckyChanceRepository;

    @Resource
    SysCommodityRepository commodityRepository;


    @GetMapping(value = "wxq")
    public String test() throws ApiException {
        return "success";
    }

    @GetMapping(value = "batchSaveBrowseBaby")
    public String batchSaveBrowseBaby(List<SysCommodity> list){
        commodityRepository.saveAll(list);
        return "操作成功!";
    }



}
