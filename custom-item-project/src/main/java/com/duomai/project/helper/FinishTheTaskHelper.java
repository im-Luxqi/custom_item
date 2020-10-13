package com.duomai.project.helper;

import com.duomai.project.product.general.enums.TaskTypeEnum;
import com.duomai.project.product.general.repository.SysGeneralTaskRepository;
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
    private SysGeneralTaskRepository sysGeneralTaskRepository;

    //获取目前粉丝签到的次数
    public long getFinishTheTaskNum(String buyerNick) {
        return sysGeneralTaskRepository.countByBuyerNickAndTaskType(buyerNick, TaskTypeEnum.SIGN);
    }

    /**
     * 随机取n个不同数据
     *
     * @param list 集合
     * @param num  随机的个数
     * @param nowList 结果容器
     */
    public List randowList(List list,List nowList, int num) {
        if (list.size() < num) {
            return list;
        }
        Random random = new Random();
        int r = random.nextInt(list.size());
        nowList.add(list.get(r));
        list.remove(r);
        if (nowList.size() < num) {
            randowList(list,nowList,num);
        }
        return nowList;
    }


}
