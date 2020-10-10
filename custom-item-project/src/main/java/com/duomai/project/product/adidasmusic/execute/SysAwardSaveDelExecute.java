package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.repository.SysAwardRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @内容：封网新增删除奖品
 * @创建人：lyj
 * @创建时间：2020.10.09
 */
@Component
public class SysAwardSaveDelExecute implements IApiExecute {

    @Resource
    private SysAwardRepository sysAwardRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 获取参数
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        // 用于删除
        String ids = object.getString("ids");
        // 用于新增
        JSONArray array = object.getJSONArray("awards");
        List<SysAward> sysAwards = array.toJavaList(SysAward.class);

        if (StringUtils.isNotBlank(ids)){
            String[] sql = ids.split(",");
            for (String s : sql){
                sysAwardRepository.deleteById(s);
            }
            return YunReturnValue.ok("操作成功！");
        }

        if (!sysAwards.isEmpty()){
            sysAwardRepository.saveAll(sysAwards);
        }
        return YunReturnValue.ok("操作成功！");
    }
}
