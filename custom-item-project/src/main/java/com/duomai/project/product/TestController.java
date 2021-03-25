package com.duomai.project.product;

import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.configuration.SysCustomProperties;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.repository.SysSettingAwardRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.response.AlibabaBenefitSendResponse;
import com.taobao.api.response.CrmPointChangeResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    SysSettingAwardRepository sysSettingAwardRepository;

    @Autowired
    ITaobaoAPIService taobaoAPIService;

    @Autowired
    SysLuckyChanceRepository sysLuckyChanceRepository;

    @GetMapping(value = "wxq")
    public String test() throws Exception {

//        taobaoAPIService.isMember("夏0122nZ6No9jp65GKADf2oNRw5f4Jgluem5SlKj13PxUtg=");
//        AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon("AAEq2PUFANe32XlcpaKU0FVM", "f199673472814600aa272ea73edd0328");
        CrmPointChangeResponse crmPointChangeResponse = taobaoAPIService.changePoint("夏0122nZ6No9jp65GKADf2oNRw5f4Jgluem5SlKj13PxUtg=", 10L);
        if (crmPointChangeResponse == null || !crmPointChangeResponse.isSuccess()) {
            log.info(crmPointChangeResponse != null ? crmPointChangeResponse.getMsg() : "rsp is null");
        }
        return crmPointChangeResponse != null ? crmPointChangeResponse.getMsg() : "rsp is null";
    }

}
