package com.duomai.project.api.admin.controller;

import com.alibaba.fastjson.JSON;
import com.duomai.project.configuration.annotation.LoginRequired;
import com.duomai.project.configuration.dto.UserInfoForCookie;
import com.duomai.project.configuration.tool.AES;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin")
public class AdminController {
    static String userName = "wxq";
    static String userPassward = "123";

    @GetMapping("/loginIndex")
    public String index() {
        return "admin/loginIndex";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map login(String name, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map resultMap = new HashMap<String, Object>();
        boolean successLoginFlag = false;
        if (userName.equals(name) && userPassward.equals(password)) {

            UserInfoForCookie userInfoForCookie = new UserInfoForCookie();
            userInfoForCookie.setUserName(userName);
            userInfoForCookie.setPassword(userPassward);
            //声明cookie-保存cookie
            Cookie c = new Cookie("H5CustomerAutoLogin", AES.Encrypt(
                    JSON.toJSONString(userInfoForCookie), "47HTvV7XOty3bQlE"));
            c.setMaxAge(60 * 60 * 2);//两个小时
            c.setPath(request.getContextPath());
            response.addCookie(c);
            //用户信息存入session
            request.getSession().setAttribute("H5CustomerXinHuaWang", userInfoForCookie);
            successLoginFlag = true;
        }
        resultMap.put("success", successLoginFlag);
        return resultMap;
    }

    @LoginRequired()
    @GetMapping("/manage")
    public String manage(Model model, Integer again, String target) {
        return "admin/manage";
    }
}
