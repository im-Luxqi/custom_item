package com.duomai.new_custom_base;

import com.duomai.new_custom_base.api.product.gen.repository.GenTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class NewCustomBaseApplicationTests {
    @Autowired
    DataSource dataSource;
    //    @Autowired
//    CityService cityService;
    @Autowired
    GenTableRepository genTableRepository;

    @Test
    void contextLoads() throws SQLException {
        System.out.println("dataSource: " + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("connection: " + connection);
        connection.close();
    }

    @Test
    void mptest() {
        genTableRepository.selectDbTableListByNames(null);
//        iGenTableService.getById(1);
//        City byId = cityService.getById(1);
//        System.out.println("-----------------------------" + byId.getCityName());
    }

}
