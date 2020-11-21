package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.AddCommodityDto;
import com.duomai.project.product.general.entity.SysSettingCommodity;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.repository.SysSettingCommodityRepository;
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
 * @time 2020-10-06
 */
@Service
public class DmBrowseBabySaveDelExecute implements IApiExecute {

    @Resource
    private SysSettingCommodityRepository commodityRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request,
                                     HttpServletResponse response) {

        //取参
        JSONObject object = sysParm.getApiParameter().findJsonObjectAdmjson();
        //删除
        String ids = object.getString("ids");
        //新增、修改
        JSONArray array = object.getJSONArray("sysCommodity");

        if (StringUtils.isNotBlank(ids)) {
            String[] spl = ids.split(",");
            for (String s : spl) {
                commodityRepository.deleteById(s);
            }
            return YunReturnValue.ok("操作成功!");
        }

        if(array != null) {
            List<AddCommodityDto> list = array.toJavaList(AddCommodityDto.class);
            if (!list.isEmpty()) {
                List<SysSettingCommodity> commodities = new ArrayList<>();
                for (AddCommodityDto dto : list) {
                    SysSettingCommodity commodity = new SysSettingCommodity();
                    commodities.add(commodity.setName(dto.getName())
                            .setCreateTime(dto.getCreateTime())
                            .setCommoditySort(dto.getCommoditySort())
                            .setPrice(dto.getPrice())
                            .setType(AwardTypeEnum.valueOf(dto.getType()))
                            .setImg(dto.getImg())
                            .setNumId(dto.getNumId())
                    );
                }
                commodityRepository.saveAll(commodities);
            }
        }

        return YunReturnValue.ok("操作成功!");
    }
}
