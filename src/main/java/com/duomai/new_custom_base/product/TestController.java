package com.duomai.new_custom_base.product;

import com.duomai.new_custom_base.product.gen.service.IGenTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-27 17:07
 */
@RestController
public class TestController {
    @Autowired
    IGenTableService iGenTableService;

    @GetMapping("/test")
    public String test() {
        iGenTableService.getById(1);
        return "success";
    }
}
