package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.adidasmusic.domain.CusBigWheel;
import com.duomai.project.product.adidasmusic.service.ICusBigWheelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description 封网准备接口 新增、删除尖货大咖活动
 * @author cjw
 * @date 2020-10-06
 */
@Service
public class DmCusBigWheelSaveDelExecute implements IApiExecute {

    @Resource
    private ICusBigWheelService cusBigWheelService;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        String ids = object.getString("ids");
        String bigWheel = object.getString("bigWheel");
        List<CusBigWheel> cusBigWheels = JSONObject.parseArray(bigWheel,CusBigWheel.class);

        //ids不为空为删除
        if (StringUtils.isNotBlank(ids)) {
            String[] spl = ids.split(",");
            for (String s : spl) {
                cusBigWheelService.removeById(s);
            }
            return YunReturnValue.ok("操作成功!");
        }

        //cusBigWheels 不为空
        if(!cusBigWheels.isEmpty()){
            cusBigWheelService.saveBatch(cusBigWheels);
        }

        return YunReturnValue.ok("操作成功!");
    }
}
