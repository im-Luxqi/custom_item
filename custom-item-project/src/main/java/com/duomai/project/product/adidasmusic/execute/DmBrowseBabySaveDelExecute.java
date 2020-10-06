package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.repository.SysCommodityRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cjw
 * @description 新增、删除浏览宝贝
 * @date 2020-10-06
 */
@Service
public class DmBrowseBabySaveDelExecute implements IApiExecute {

    @Resource
    private SysCommodityRepository commodityRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        String ids = object.getString("ids");
        String sysCommodity = object.getString("sysCommodity");
        List<SysCommodity> sysCommodity1 = JSONObject.parseArray(sysCommodity,SysCommodity.class);
        //ids不为空为删除
        if (StringUtils.isNotBlank(ids)) {
            String[] spl = ids.split(",");
            for (String s : spl) {
                commodityRepository.deleteById(s);
            }
            return YunReturnValue.ok("操作成功!");
        }

        //sysCommodity1 不为空
        if(!sysCommodity1.isEmpty()){
            commodityRepository.saveAll(sysCommodity1);
        }

        return YunReturnValue.ok("操作成功!");
    }
}
