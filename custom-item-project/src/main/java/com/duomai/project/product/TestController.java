package com.duomai.project.product;

import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.configuration.SysCustomProperties;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.enums.LuckyChanceFrom;
import com.duomai.project.product.general.repository.SysAwardRepository;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.helper.LuckyDrawHelper;
import com.duomai.project.helper.ProjectHelper;
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
    public XyReturn test() throws ApiException {
//        ActBaseSetting actBaseSetting = projectHelper.actBaseSettingFind();
//        String openId = "tb138805818";
//        String buyernick = "t011OKZDdxsgi2BgiM54DcAqE4trS9hcL3O43H6xfPR9tU=";
//        Date requestTime = new Date();
//        List<SysAward> all = sysAwardRepository.findAllByOrderByLuckyValue();
//        try {
//            projectHelper.customInit(null);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
        AlibabaBenefitSendResponse alibabaBenefitSendResponse = taobaoAPIService.sendTaobaoCoupon("AAHdN7xBAMPS9ht6emzVRz-2", "8a75f0a00dcb42f0a76905f4847eb96b",
                sysCustomProperties.getCustomConfig().getAppName(), sysCustomProperties.getCustomConfig().getSessionkey());
//        luckyDrawHelper.sendLuckyChance("s01bPY6OKAXoXEBLuS0WXELkWL65KSN4Xgj0x2NaS0H3n4=", LuckyChanceFrom.ORDER_COMMIT,200,"zhujinya");
//        XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), openId,openId,
//                actBaseSetting.getActStartTime().getTime(), actBaseSetting.getActEndTime().getTime(), buyernick, requestTime);
        return null;

    }
}
