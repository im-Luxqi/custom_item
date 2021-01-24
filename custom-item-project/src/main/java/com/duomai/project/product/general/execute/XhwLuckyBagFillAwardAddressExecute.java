package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.product.general.dto.AddressInfoFillDto;
import com.duomai.project.product.general.entity.XhwAwardRecord;
import com.duomai.project.product.general.repository.XhwAwardRecordRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import com.duomai.project.tool.ProjectTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/*对于抽中的实物奖品，完善地址信息
 * @description
 * @create by 王星齐
 * @time 2020-07-31 10:30:29
 **/
@Component
public class XhwLuckyBagFillAwardAddressExecute implements IApiExecute {
    @Autowired
    private XhwAwardRecordRepository xhwAwardRecordRepository;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {


        /*1.校验参数*/
        AddressInfoFillDto beautyAdmjson = sysParm.getApiParameter().findBeautyAdmjson(AddressInfoFillDto.class);
        ProjectTools.validateParam(beautyAdmjson);

        Optional<XhwAwardRecord> maybeRecord = xhwAwardRecordRepository.findById(beautyAdmjson.getId());
        Assert.isTrue(maybeRecord.isPresent(), "不存在的中奖记录");
        XhwAwardRecord record = maybeRecord.get();

        xhwAwardRecordRepository.save(record.setIsFill(BooleanConstant.BOOLEAN_YES)
                .setReceviceCity(beautyAdmjson.getReceviceCity())
                .setReceviceDistrict(beautyAdmjson.getReceviceDistrict())
                .setReceviceName(beautyAdmjson.getReceviceName())
                .setRecevicePhone(beautyAdmjson.getRecevicePhone())
                .setReceviceProvince(beautyAdmjson.getReceviceProvince())
                .setReceviceTime(sysParm.getRequestStartTime())
                .setReceviceTimeString(CommonDateParseUtil.date2string(sysParm.getRequestStartTime(),"yyyy-MM-dd"))
                .setReceviceAddress(beautyAdmjson.getReceviceAddress()));
        return YunReturnValue.ok("完善地址成功");
    }
}
