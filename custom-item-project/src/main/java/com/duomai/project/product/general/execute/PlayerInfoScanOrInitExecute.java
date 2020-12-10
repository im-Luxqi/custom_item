package com.duomai.project.product.general.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.constants.BooleanConstant;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.api.taobao.ITaobaoAPIService;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.dto.ActBaseSettingDto;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.enums.PlayActionEnum;
import com.duomai.project.product.general.repository.SysCustomRepository;
import com.duomai.project.product.general.repository.SysGameBoardDailyRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 玩家信息扫描，首次完成初始化操作
 *
 * @author im-luxqi
 * @description
 * @create by 王星齐
 */
@Component
public class PlayerInfoScanOrInitExecute implements IApiExecute {

    @Autowired
    private SysCustomRepository sysCustomRepository;
    @Resource
    private ITaobaoAPIService taobaoAPIService;
    @Resource
    private SysGameBoardDailyRepository sysGameBoardDailyRepository;
    @Resource
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*1.查询玩家信息,未查询到初始化玩家信息*/
        String buyerNick = sysParm.getApiParameter().getYunTokenParameter().getBuyerNick();
        Date requestStartTime = sysParm.getRequestStartTime();
        String requestStartTimeString = CommonDateParseUtil.date2string(requestStartTime, "yyyy-MM-dd");
        SysCustom sysCustom = sysCustomRepository.findByBuyerNick(buyerNick);

        ActBaseSettingDto actSetting = projectHelper.actBaseSettingFind();
        boolean lastDay = requestStartTime.after(actSetting.getActLastTime());
        SysGameBoardDaily daily = sysGameBoardDailyRepository.findFirstByBuyerNickAndCreateTimeString(buyerNick, requestStartTimeString);
        if (sysCustom == null) {
            //保存粉丝信息
            boolean historyMember = taobaoAPIService.isMember(buyerNick);
            SysCustom temp = new SysCustom().setBuyerNick(buyerNick)
                    .setCreateTime(requestStartTime)
                    .setOpenId(sysParm.getApiParameter().getYunTokenParameter().getOpenUId())
                    .setHaveAuthorization(BooleanConstant.BOOLEAN_NO)
                    .setMemberWayFrom(historyMember ? MemberWayFromEnum.HISTROY_MEMBER : MemberWayFromEnum.NON_MEMBER)
                    .setFollowWayFrom(FollowWayFromEnum.UNDIFIND)
                    .setPlayParty("party1")
                    .setCurrentAction(PlayActionEnum.playwith_snowman)
                    .setHaveInviteFriend(BooleanConstant.BOOLEAN_NO)
                    .setHaveBrowseGoods(BooleanConstant.BOOLEAN_NO)
                    .setHaveSpendGoods(BooleanConstant.BOOLEAN_NO)
                    .setStarValue(0)
                    ;
            //最后一天
            if (lastDay) {
                temp.setPlayParty("party1,party2,party3");
                temp.setCurrentAction(PlayActionEnum.party3_ing);
            }
            sysCustom = sysCustomRepository.save(temp);

            daily = sysGameBoardDailyRepository.save(new SysGameBoardDaily()
                    .setBuyerNick(buyerNick)
                    .setCreateTime(requestStartTime)
                    .setCreateTimeString(requestStartTimeString)
                    .setGameSnowman(0)
                    .setGamePenguin(0)
                    .setGameBear(0)
                    .setGameLamp(0)
                    .setGameBalloon(0)
                    .setGameDog(0)
                    .setFirstGameSnowman(BooleanConstant.BOOLEAN_YES)
                    .setFirstGamePenguin(BooleanConstant.BOOLEAN_YES)
                    .setFirstGameBear(BooleanConstant.BOOLEAN_YES)
                    .setFirstGameLamp(BooleanConstant.BOOLEAN_YES)
                    .setFirstGameTent(BooleanConstant.BOOLEAN_YES)
                    .setFirstGameDog(BooleanConstant.BOOLEAN_YES)
                    .setBearQuestionChance(2)
            );
        } else {
            //最后一天
            if (!"party1,party2,party3".equals(sysCustom.getPlayParty())
                    && lastDay) {
                sysCustom.setPlayParty("party1,party2,party3");
                sysCustom.setCurrentAction(PlayActionEnum.party3_ing);
                sysCustom = sysCustomRepository.save(sysCustom);
            }
            if (daily == null) {
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

                daily = sysGameBoardDailyRepository.save(new SysGameBoardDaily()
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
                );
            }
        }

        /*2.组织返回值*/
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();

        boolean have_authorization = BooleanConstant.BOOLEAN_YES.equals(sysCustom.getHaveAuthorization());
        resultMap.put("buyer_nick", sysCustom.getBuyerNick());
        resultMap.put("znick", sysCustom.getZnick());
        resultMap.put("have_authorization", have_authorization);
        resultMap.put("only_go_party3", lastDay);
        resultMap.put("can_go_party", sysCustom.getPlayParty());
        resultMap.put("current_action", sysCustom.getCurrentAction());
        return YunReturnValue.ok(resultMap, "获取玩家信息," +
                "【提示：】" +
                "have_authorization = false ---> 表示尚未授权,默认没有头像,真实昵称为未授权" +
                "only_go_party3 =  true  ---> 表示当前活动处于最后一天，所有玩家只展示 场景3，优先级大于玩家自身权限" +
                "can_go_party ---> 表示玩家的场景权限,三种返回值（party1）(party1,party2)(party1,party2,party3)" +
                "current_action ---> 表示玩家跟随指引的进度"
        );
    }
}