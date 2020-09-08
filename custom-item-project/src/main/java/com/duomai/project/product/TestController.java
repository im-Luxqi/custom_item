package com.duomai.project.product;

import com.duomai.project.product.demo.service.ICityService;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.tool.ProjectHelper;
import com.taobao.api.DefaultTaobaoClient;
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
    ICityService cityService;


    @Autowired
    ProjectHelper projectHelper;


    @Autowired
    DefaultTaobaoClient defaultTaobaoClient;


    @Autowired
    SysLuckyChanceRepository sysLuckyChanceRepository;

    @GetMapping(value = "wxq")
    public XyReturn test() {
        return null;
    }
}
