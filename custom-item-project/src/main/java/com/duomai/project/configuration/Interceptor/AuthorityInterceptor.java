package com.duomai.project.configuration.Interceptor;

import com.alibaba.fastjson.JSON;
import com.duomai.project.configuration.annotation.LoginRequired;
import com.duomai.project.configuration.dto.UserInfoForCookie;
import com.duomai.project.configuration.tool.AES;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @description
 * @create by 王星齐
 * @date 2019-11-03 14:42
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {
    /*拦截器
     * @description
     * @create by 王星齐
     * @time 2019-11-03 14:44:05
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断是否需要登录
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);

        if (methodAnnotation != null) {
            request.getSession().removeAttribute("H5CustomerXinHuaWang");
            //尝试从cookie中获取权限
            Cookie[] cs = request.getCookies();
            if (cs != null) {
                for (Cookie c : cs) {
                    if (c.getName().equals("H5CustomerAutoLogin") && c.getValue() != null) {//如果存在自动登录的cookie
                        String value = c.getValue();//用户名称
                        //尝试重新登录
                        UserInfoForCookie userInfoForCookie = JSON.parseObject(
                                AES.Decrypt(value, "47HTvV7XOty3bQlE"), UserInfoForCookie.class);
                        relogin(userInfoForCookie, request, response);
                        break;
                    }
                }
            }
            if (request.getSession().getAttribute("H5CustomerXinHuaWang") == null) {
                response.sendRedirect("/admin/loginIndex");
                return false;
            }
        }
        return true;
    }


    private void relogin(UserInfoForCookie userInfoForCookie, HttpServletRequest request, HttpServletResponse response) {
        //声明cookie-保存cookie
        Cookie c = null;
        try {
            c = new Cookie("H5CustomerAutoLogin", AES.Encrypt(
                    JSON.toJSONString(userInfoForCookie), "47HTvV7XOty3bQlE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setMaxAge(60 * 60 * 2);//两个小时
        c.setPath(request.getContextPath());
        response.addCookie(c);
        //用户信息存入session
        request.getSession().setAttribute("H5CustomerXinHuaWang", userInfoForCookie);
    }
}