package com.duomai.project.product.adidasmusic.execute;

import com.alibaba.fastjson.JSONObject;
import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.adidasmusic.domain.CusBigWheel;
import com.duomai.project.product.adidasmusic.service.ICusBigWheelService;
import com.duomai.project.product.general.dto.PageListDto;
import com.duomai.project.product.general.enums.CusBigWheelStateEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        projectHelper.checkoutMultipleCommit(sysParm,this);

        PageListDto beautyAdmjson = sysParm.getApiParameter().findBeautyAdmjson(PageListDto.class);
        beautyAdmjson.startPage();
        List<CusBigWheel> cusBigWheelList = iCusBigWheelService.query().list();
        for (CusBigWheel cusBigWheel : cusBigWheelList){
            if (cusBigWheel.getStartTime().after(new Date())){  // 开始时间大于当前时间
                cusBigWheel.setState(CusBigWheelStateEnum.NOT_STARTING);
            }
            if (cusBigWheel.getEndTime().before(new Date())){   // 结束时间小于当前时间
                cusBigWheel.setState(CusBigWheelStateEnum.STOPPING);
            }
            if (cusBigWheel.getStartTime().before(new Date()) && cusBigWheel.getEndTime().after(new Date())){
                cusBigWheel.setState(CusBigWheelStateEnum.PROGRESSING);
            }
        }
        beautyAdmjson.setResultList(cusBigWheelList);
        return YunReturnValue.ok(beautyAdmjson,"尖货大咖记录");
    }
}