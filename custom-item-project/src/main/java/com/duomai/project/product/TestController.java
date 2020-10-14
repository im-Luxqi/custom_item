package com.duomai.project.product;

import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.configuration.SysCustomProperties;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

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

    @GetMapping(value = "wxq")
    public String test() throws ApiException {

        taobaoAPIService.isMember("夏01p2dBwi4yiMon9yxJTfxmCkprZqrpmO3kFuSpghDIeqQ=");
        AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon("AAEA4uHTAMeks3ANjnKYXoKb", "03634718fd484633b4de2ec6bea5ee98");

        return "success";
    }

}
