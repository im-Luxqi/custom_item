package com.duomai.project.helper;

import com.duomai.project.product.general.repository.SysTaskMemberOrFollowRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @author cjw
 * @description 活动任务相关
 * @date 2020-10-02
 */
@Component
public class FinishTheTaskHelper {

    @Resource
    private SysTaskMemberOrFollowRepository sysTaskMemberOrFollowRepository;

    //获取目前粉丝签到的次数
    public long getFinishTheTaskNum(String buyerNick) {
//        return sysTaskMemberOrFollowRepository.countByBuyerNickAndTaskType(buyerNick, TaskTypeEnum.SIGN);
        return 0;
    }

    /**
     * 随机取n个不同数据
     *
     * @param list    集合
     * @param num     随机的个数
     * @param nowList 结果容器
     */
    public List randowList(List list, List nowList, int num) {
        Random random = new Random();
        int r = random.nextInt(list.size());
        nowList.add(list.get(r));
        list.remove(r);
        if (nowList.size() < num) {
            if (!list.isEmpty()) {
                randowList(list, nowList, num);
            }
        }
        return nowList;
    }


}
