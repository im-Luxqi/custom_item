//package com.duomai.project.product.adidasmusic.execute;
//
//import com.alibaba.fastjson.JSONObject;
//import com.duomai.common.base.execute.IApiExecute;
//import com.duomai.common.constants.BooleanConstant;
//import com.duomai.common.dto.ApiSysParameter;
//import com.duomai.common.dto.YunReturnValue;
//import com.duomai.project.helper.ProjectHelper;
//import com.duomai.project.product.general.dto.ActBaseSettingDto;
//import com.duomai.project.product.general.entity.SysBrowseLog;
//import com.duomai.project.product.general.entity.SysCustom;
//import com.duomai.project.product.general.entity.SysLuckyChance;
//import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
//import com.duomai.project.product.general.repository.SysBrowseLogRepository;
//import com.duomai.project.product.general.repository.SysCustomRepository;
//import com.duomai.project.product.general.repository.SysLuckyChanceRepository;
//import com.duomai.project.tool.CommonDateParseUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Date;
//import java.util.List;
//
///**
// * @内容：任务页面 浏览商品操作
// * @创建人：lyj
// * @创建时间：2020.9.30
// * */
//@Component
//public class GeneralTaskBrowseOperateExecute implements IApiExecute {
//    @Autowired
//    private SysCustomRepository sysCustomRepository;
//    @Autowired
//    private SysBrowseLogRepository sysBrowseLogRepository;
//    @Autowired
//    private SysLuckyChanceRepository sysLuckyChanceRepository;
//    @Autowired
//    private ProjectHelper projectHelper;
//
//    @Override
//    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        //预防连点
//        projectHelper.checkoutMultipleCommit(sysParm,this);
//        ActBaseSettingDto actBaseSettingDto = projectHelper.actBaseSettingFind();
//         projectHelper.actTimeValidate();
//
//        //获取参数
//        JSONObject object =sysParm.getApiParameter().findJsonObjectAdmjson();
//        Long numId = object.getLong("numId");
//        Assert.notNull(numId, "商品id不能为空");
//
//        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
//        // 校验玩家是否存在
//        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);
//        Assert.notNull(sysCustom, "不存在该玩家");
//
//        Date date = sysParm.getRequestStartTime();
//        // 校验
//        List<SysBrowseLog> browseLog = sysBrowseLogRepository.findByBuyerNickAndCreateTimeBetween(buyerNick,
//                CommonDateParseUtil.getStartTimeOfDay(date), CommonDateParseUtil.getEndTimeOfDay(date));
//        if (browseLog.size() > 0){
//            Assert.isNull(browseLog, "操作已完成！");
//        }
//        /*保存操作日志*/
//        SysBrowseLog sysBrowseLog = new SysBrowseLog();
//        sysBrowseLogRepository.save(sysBrowseLog.setBuyerNick(buyerNick)
//                .setCreateTime(sysParm.getRequestStartTime())
//                .setNumId(numId));
//        /*插入一条抽奖机会来源*/
//        SysLuckyChance luckyChance = new SysLuckyChance();
//        sysLuckyChanceRepository.save(luckyChance.setBuyerNick(buyerNick)
//                .setGetTime(date)
//                .setChanceFrom(LuckyChanceFromEnum.BROWSE)
//                .setIsUse(BooleanConstant.BOOLEAN_NO));
//        return YunReturnValue.ok("操作成功!");
//    }
//}
