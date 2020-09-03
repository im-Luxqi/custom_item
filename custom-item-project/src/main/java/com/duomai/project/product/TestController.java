package com.duomai.project.product;

import com.duomai.project.product.demo.service.ICityService;
import com.duomai.project.product.general.dto.XyReturn;
import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
import com.duomai.project.tool.ProjectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
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
    ICityService cityService;


    @Autowired
    ProjectHelper projectHelper;


    @Autowired
    SysLuckyChanceRepository sysLuckyChanceRepository;


    @GetMapping(value = "wxq")
    public XyReturn test() {
        final String orderSn = "1204002036979524301";
        final String openID = "2088302134043800";



        sysLuckyChanceRepository.tryReduceOne("t0");
//        projectHelper.findOrdersByOpenId(System.currentTimeMillis(),openID);
//        XyReturn orderBySn = projectHelper.findOrderBySn(System.currentTimeMillis(), orderSn);
        return null;
    }
    @GetMapping(value = "wxq1")
    public XyReturn test1() {
        List<String> strings = new ArrayList<>();
        strings.add("aaaa");
        strings.add("bbbb");
        strings.add("cccc");

        strings.contains("aaaa");

        return null;
    }
}
