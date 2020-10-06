package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.AddCommodityDto;
import com.duomai.project.product.general.entity.SysCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.repository.SysCommodityRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjw
 * @description 封网准备接口 新增、删除浏览宝贝
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
        JSONArray array = object.getJSONArray("sysCommodity");
        List<AddCommodityDto> dtos = array.toJavaList(AddCommodityDto.class);

        //ids不为空为删除
        if (StringUtils.isNotBlank(ids)) {
            String[] spl = ids.split(",");
            for (String s : spl) {
                commodityRepository.deleteById(s);
            }
            return YunReturnValue.ok("操作成功!");
        }

        //sysCommodity1 不为空
        if (!dtos.isEmpty()) {
            List<SysCommodity> commodities = new ArrayList<>();
            for (AddCommodityDto dto : dtos) {
                SysCommodity commodity = new SysCommodity();
                commodities.add(commodity.setName(dto.getName())
                        .setPrice(dto.getPrice())
                        .setType(AwardTypeEnum.valueOf(dto.getType()))
                        .setImg(dto.getImg())
                        .setNumId(dto.getNumId())
                );
            }
            commodityRepository.saveAll(commodities);
        }

        return YunReturnValue.ok("操作成功!");
    }
}
