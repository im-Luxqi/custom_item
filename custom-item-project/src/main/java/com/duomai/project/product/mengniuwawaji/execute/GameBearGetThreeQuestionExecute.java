package com.duomai.project.product.mengniuwawaji.execute;

import com.duomai.common.base.execute.IApiExecute;
import com.duomai.common.dto.ApiSysParameter;
import com.duomai.common.dto.YunReturnValue;
import com.duomai.project.helper.ProjectHelper;
import com.duomai.project.product.general.entity.SysBearQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 场景1 白熊 获取三道新题目
 *
 * @author 王星齐
 * @description
 * @create 2020/11/18 18:14
 */
@Component
public class GameBearGetThreeQuestionExecute implements IApiExecute {

    @Autowired
    private ProjectHelper projectHelper;

    @Override
    public YunReturnValue ApiExecute(ApiSysParameter sysParm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SysBearQuestion> allQuestion = projectHelper.getAllQuestion();
        List<SysBearQuestion> three = new ArrayList<>();
        Random random = new Random();
        three.add(allQuestion.get(random.nextInt(allQuestion.size())));
        three.add(allQuestion.get(random.nextInt(allQuestion.size())));
        three.add(allQuestion.get(random.nextInt(allQuestion.size())));
        return YunReturnValue.ok(three, "白熊 获取三道新题目");
    }
}




