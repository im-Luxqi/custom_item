package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysKeyValue;
import com.duomai.project.product.general.repository.SysKeyValueRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cjw
 * @description 封网备用 修改活动配置表信息
 * @time 2020-10-10
 */
@Service
public class DmModifyActivitySettingExecute implements IApiExecute {

    @Resource
    private SysKeyValueRepository sysKeyValueRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        //删除
        String ids = object.getString("ids");
        //新增、修改
        JSONArray keyValue = object.getJSONArray("keyValue");
        List<SysKeyValue> keyValues = keyValue.toJavaList(SysKeyValue.class);

        if (StringUtils.isNotBlank(ids)) {
            String[] spl = ids.split(",");
            for (String s : spl) {
                sysKeyValueRepository.deleteById(s);
            }
            return YunReturnValue.ok("操作成功!");
        }

        if (!keyValues.isEmpty()) {
            sysKeyValueRepository.saveAll(keyValues);
        }
        return YunReturnValue.ok("操作成功!");
    }
}
