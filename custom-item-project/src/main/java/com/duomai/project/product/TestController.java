package com.duomai.project.product;

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
    SysLuckyChanceRepository sysLuckyChanceRepository;

    @GetMapping(value = "wxq")
    public XyReturn test() {
        ActBaseSetting actBaseSetting = projectHelper.actBaseSettingFind();
        String openId = "spudcfd";
        String buyernick = "s01bPY6OKAXoXEBLuS0WXELkWL65KSN4Xgj0x2NaS0H3n4=";
        Date requestTime = new Date();
//        luckyDrawHelper.sendLuckyChance("s01bPY6OKAXoXEBLuS0WXELkWL65KSN4Xgj0x2NaS0H3n4=", LuckyChanceFrom.ORDER_FINISH,200,"zhuyajin");
        XyReturn ordersByOpenId = projectHelper.findOrdersByOpenId(System.currentTimeMillis(), openId,openId,
                actBaseSetting.getActStartTime().getTime(), actBaseSetting.getActEndTime().getTime(), buyernick, requestTime);
        return null;
    }
}
