package com.duomai.project.product.adidasmusic.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.domain.CusBigWheel;
import com.duomai.project.product.adidasmusic.dto.CusBigWheelDto;
import com.duomai.project.product.adidasmusic.service.ICusBigWheelService;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.enums.CusBigWheelStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @内容：尖货大咖 活动标签
 * @创建人：lyj
 * @创建时间：
 */
@Component
public class CusBigWheelListExecute implements IApiExecute {
    @Autowired
    private ICusBigWheelService iCusBigWheelService;
    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //预防连点


        PageListDto beautyAdmjson = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);
        beautyAdmjson.startMybatisPage();
        List<CusBigWheel> cusBigWheelList = iCusBigWheelService.query().list();
        List<CusBigWheelDto> result = new ArrayList<>();
        Date date = sysParm.getRequestStartTime();
        for (CusBigWheel cusBigWheel : cusBigWheelList) {
            CusBigWheelDto dto = getObject(cusBigWheel);
            if (cusBigWheel.getStartTime().after(date)) {  // 开始时间大于当前时间
                dto.setState(CusBigWheelStateEnum.NOT_STARTING);
            }
            if (cusBigWheel.getEndTime().before(date)) {   // 结束时间小于当前时间
                dto.setState(CusBigWheelStateEnum.STOPPING);
            }
            if (cusBigWheel.getStartTime().before(date) && cusBigWheel.getEndTime().after(date)) {
                dto.setState(CusBigWheelStateEnum.PROGRESSING);
            }
            result.add(dto);
        }
        beautyAdmjson.setResultList(result);
        return YunReturnValue.ok(beautyAdmjson, "尖货大咖记录");
    }

    // 获取Dto对象
    private CusBigWheelDto getObject(CusBigWheel cusBigWheel) {
        CusBigWheelDto dto = new CusBigWheelDto();
        dto.setTitle(cusBigWheel.getTitle());
        dto.setContext(cusBigWheel.getContext());
        dto.setImg(cusBigWheel.getImg());
        dto.setStartTime(cusBigWheel.getStartTime());
        dto.setEndTime(cusBigWheel.getEndTime());
        dto.setFlyLink(cusBigWheel.getFlyLink());
        return dto;
    }
}
