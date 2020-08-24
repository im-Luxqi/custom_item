package com.duomai.project.product;

import com.duomai.project.product.demo.domain.City;
import com.duomai.project.product.demo.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;

/** test
 * @description
 */
@RestController
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    DataSource dataSource;

    @Autowired
    ICityService cityService;

    @GetMapping(value = "wxq")
    public String test() {
        City city = new City();
//        city.setId("1L");
        city.setCityName("ccc");
        List<City> list = cityService.list();
        cityService.save(city);
        return "success";
    }
}
