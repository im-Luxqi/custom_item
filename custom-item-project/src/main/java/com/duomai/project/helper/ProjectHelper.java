package com.duomai.project.helper;

import com.duomai.common.constants.BooleanConstant;
import com.duomai.project.configuration.annotation.JoinMemcache;
import com.duomai.project.helper.constants.ActSettingConstant;
import com.duomai.project.helper.constants.ActTreeWinConstant;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.dto.ActTreeWinDto;
import com.duomai.project.product.general.entity.SysBearQuestion;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.entity.SysSettingKeyValue;
import com.duomai.project.product.general.repository.SysBearQuestionRepository;
import com.duomai.project.product.general.repository.SysGameBoardDailyRepository;
import com.duomai.project.product.general.repository.SysSettingKeyValueRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 活动 常规操作
 *
 * @description 【帮助类目录】
 * 1.任务配置--信息获取
 * 2.活动配置--信息获取
 * 3.检验时候为活动期间
 * @create by 王星齐
 * @date 2020-08-26 16:52
 */
@Slf4j
@Component
public class ProjectHelper {
    @Autowired
    private SysSettingKeyValueRepository sysSettingKeyValueRepository;
    @Autowired
    private SysBearQuestionRepository sysBearQuestionRepository;

    @Autowired
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;


    /**
     * 2.活动配置--信息获取
     *
     * @description
     * @create by 王星齐
     * @time 2020-08-26 20:03:20
     */
    @JoinMemcache()
    public ActBaseSettingDto actBaseSettingFind() {
        List<SysSettingKeyValue> byType = sysSettingKeyValueRepository.findByType(ActSettingConstant.TYPE_ACT_SETTING);
        Map<String, String> collect = byType.stream().collect(Collectors.toMap(SysSettingKeyValue::getK, SysSettingKeyValue::getV));
        return new ActBaseSettingDto().setActRule(collect.get(ActSettingConstant.ACT_RULE))
                .setActLastTime(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_LAST_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setActStartTime(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_START_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setActEndTime(CommonDateParseUtil.getEndTimeOfDay(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ACT_END_TIME), CommonDateParseUtil.YYYY_MM_DD)))
                .setOrderStartTime(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ORDER_START_TIME), CommonDateParseUtil.YYYY_MM_DD))
                .setOrderEndTime(CommonDateParseUtil.getEndTimeOfDay(CommonDateParseUtil.string2date(collect.get(ActSettingConstant.ORDER_END_TIME), CommonDateParseUtil.YYYY_MM_DD)))
                ;
    }

    @JoinMemcache()
    public ActTreeWinDto treeWinSettingFind() {
        List<SysSettingKeyValue> byType = sysSettingKeyValueRepository.findByType(ActTreeWinConstant.TYPE_TREE_WIN);
        Map<String, String> collect = byType.stream().collect(Collectors.toMap(SysSettingKeyValue::getK, SysSettingKeyValue::getV));
        return new ActTreeWinDto()
                .setStarValueTreeLimit(Integer.valueOf(collect.get(ActTreeWinConstant.STAR_VALUE_TREE_LIMIT)))
                .setTimeTreeLimit(CommonDateParseUtil.string2date(collect.get(ActTreeWinConstant.TIME_TREE_LIMIT), CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS))
                .setTreeAwardOne(collect.get(ActTreeWinConstant.TREE_AWARD_ONE))
                .setTreeAwardTwo(collect.get(ActTreeWinConstant.TREE_AWARD_TWO))
                .setTreeAwardThree(collect.get(ActTreeWinConstant.TREE_AWARD_THREE))
                .setTreeAwardFour(collect.get(ActTreeWinConstant.TREE_AWARD_FOUR))
                ;
    }


    /**
     * 所有问题
     *
     * @return
     */
    @JoinMemcache(refreshTime = 100)
    public List<SysBearQuestion> getAllQuestion() {
        return sysBearQuestionRepository.findAll();
    }


    /* 3.检验时候为活动期间
     * @description
     *   使用场景-------->post请求
     * @create by 王星齐
     * @time 2020-08-26 20:11:04
     **/
    public void actTimeValidate() throws Exception {
        ActBaseSettingDto actBaseSettingDto = this.actBaseSettingFind();
        Date now = new Date();
        if (now.before(actBaseSettingDto.getActStartTime()))
            throw new Exception("活动尚未开始，尽情期待！");
        if (now.after(actBaseSettingDto.getActEndTime()))
            throw new Exception("活动已结束！");
    }


    public SysGameBoardDaily findTodayGameBoard(SysCustom sysCustom,Date requestStartTime) {

        String buyerNick  = sysCustom.getBuyerNick();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysGameBoardDaily todayGameBoard = sysGameBoardDailyRepository.findFirstByBuyerNickAndCreateTimeString(buyerNick, requestStartTimeString);

        if (todayGameBoard == null) {
            int firstSnowman = BooleanConstant.BOOLEAN_YES;
            int firstPenguin = BooleanConstant.BOOLEAN_YES;
            int firstBear = BooleanConstant.BOOLEAN_YES;
            int firstLamp = BooleanConstant.BOOLEAN_YES;
            int firstTent = BooleanConstant.BOOLEAN_YES;
            int firstDog = BooleanConstant.BOOLEAN_YES;
            switch (sysCustom.getCurrentAction()) {

                case playwith_snowman:
                    break;
                case playwith_penguin:
                    firstSnowman = BooleanConstant.BOOLEAN_NO;
                    break;
                case playwith_bear:
                    firstSnowman = BooleanConstant.BOOLEAN_NO;
                    firstPenguin = BooleanConstant.BOOLEAN_NO;
                    break;
                case letter_party2:
                case playwith_lamp:
                    firstSnowman = BooleanConstant.BOOLEAN_NO;
                    firstPenguin = BooleanConstant.BOOLEAN_NO;
                    firstBear = BooleanConstant.BOOLEAN_NO;
                    break;
                case playwith_tent:
                    firstSnowman = BooleanConstant.BOOLEAN_NO;
                    firstPenguin = BooleanConstant.BOOLEAN_NO;
                    firstBear = BooleanConstant.BOOLEAN_NO;
                    firstLamp = BooleanConstant.BOOLEAN_NO;
                    break;
                case playwith_dog:
                    firstSnowman = BooleanConstant.BOOLEAN_NO;
                    firstPenguin = BooleanConstant.BOOLEAN_NO;
                    firstBear = BooleanConstant.BOOLEAN_NO;
                    firstLamp = BooleanConstant.BOOLEAN_NO;
                    firstTent = BooleanConstant.BOOLEAN_NO;
                    break;
                case letter_party3:
                case party3_ing:
                    firstSnowman = BooleanConstant.BOOLEAN_NO;
                    firstPenguin = BooleanConstant.BOOLEAN_NO;
                    firstBear = BooleanConstant.BOOLEAN_NO;
                    firstLamp = BooleanConstant.BOOLEAN_NO;
                    firstTent = BooleanConstant.BOOLEAN_NO;
                    firstDog = BooleanConstant.BOOLEAN_NO;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sysCustom.getCurrentAction());
            }

            todayGameBoard = sysGameBoardDailyRepository.save(new SysGameBoardDaily()
                    .setBuyerNick(buyerNick)
                    .setCreateTime(requestStartTime)
                    .setCreateTimeString(requestStartTimeString)
                    .setGameSnowman(0)
                    .setGamePenguin(0)
                    .setGameBear(0)
                    .setGameLamp(0)
                    .setGameBalloon(0)
                    .setGameDog(0)
                    .setFirstGameSnowman(firstSnowman)
                    .setFirstGamePenguin(firstPenguin)
                    .setFirstGameBear(firstBear)
                    .setFirstGameLamp(firstLamp)
                    .setFirstGameTent(firstTent)
                    .setFirstGameDog(firstDog)
                    .setBearQuestionChance(2)
                    .setTodayRandomNum(new Random().nextInt(8) + 1)
            );
        }
        return todayGameBoard;
    }
}
