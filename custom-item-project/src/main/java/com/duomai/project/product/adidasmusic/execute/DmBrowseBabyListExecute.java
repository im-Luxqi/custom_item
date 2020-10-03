package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.repository.SysCommodityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cjw
 * @description 阿迪双十一小程序二楼 浏览宝贝
 * @date 2020-10-02
 */
@Service
public class DmBrowseBabyListExecute implements IApiExecute {

    @Resource
    private SysCommodityRepository sysCommodityRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();

        List<SysCommodity> sysCommodities = sysCommodityRepository.findAll();


        return null;
    }
}
