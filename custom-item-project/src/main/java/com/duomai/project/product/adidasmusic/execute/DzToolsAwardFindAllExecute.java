package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.repository.SysAwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DzToolsAwardFindAllExecute implements IApiExecute {

    @Autowired
    private SysAwardRepository sysAwardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*1.数据展示*/
        Map<String, Object> result = new HashMap<>();
        /*2.查询数据*/
        List<SysAward> sysAwards = sysAwardRepository.findAll();
        result.put("award_info",sysAwards);
        return YunReturnValue.ok(sysAwards, "商家奖品信息");
    }
}
