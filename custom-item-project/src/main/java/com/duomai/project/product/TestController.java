package com.duomai.project.product;

import com.duomai.project.configuration.SysCustomProperties;
import com.duomai.project.product.general.dto.ActBaseSetting;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.enums.LuckyChanceFrom;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.tool.LuckyDrawHelper;
import com.duomai.project.tool.ProjectHelper;
import com.taobao.api.DefaultTaobaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Date;

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
    SysLuckyChanceRepository sysLuckyChanceRepository;

    @GetMapping(value = "wxq")
    public XyReturn test() {
        ActBaseSetting actBaseSetting = projectHelper.actBaseSettingFind();
        String openId = "tb138805818";
        String buyernick = "t011OKZDdxsgi2BgiM54DcAqE4trS9hcL3O43H6xfPR9tU=";
        Date requestTime = new Date();
        luckyDrawHelper.sendLuckyChance("夏01p2dBwi4yiMon9yxJTfxmCkprZqrpmO3kFuSpghDIeqQ=", LuckyChanceFrom.ORDER_COMMIT,30,"wangxingqi");
//        XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), openId,openId,
//                actBaseSetting.getActStartTime().getTime(), actBaseSetting.getActEndTime().getTime(), buyernick, requestTime);
        return null;

    }
}
