package com.duomai.project.api.admin.controller;

import com.alibaba.fastjson.JSON;
import com.duomai.project.configuration.annotation.LoginRequired;
import com.duomai.project.configuration.dto.UserInfoForCookie;
import com.duomai.project.configuration.tool.AES;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.entity.XhwGroup;
import com.duomai.project.product.general.entity.XhwSetting;
import com.duomai.project.product.general.enums.AwardRunningEnum;
import com.duomai.project.product.general.repository.XhwAwardRepository;
import com.duomai.project.product.general.repository.XhwGroupRepository;
import com.duomai.project.product.general.repository.XhwSettingRepository;
import com.duomai.project.product.general.repository.XhwShowBarRepository;
import com.duomai.project.tool.CommonDateParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class AdminController {
    static String userName = "xhw";
    static String userPassward = "123";

    @Resource
    private XhwAwardRepository xhwAwardRepository;
    @Resource
    private XhwSettingRepository settingRepository;
    @Resource
    private XhwGroupRepository xhwGroupRepository;
    @Resource
    private XhwShowBarRepository xhwShowBarRepository;


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
    @GetMapping("/index")
    public String adminIndex() {
        return "admin/index";
    }


    @LoginRequired()
    @GetMapping("/virtual")
    public String virtual(Model model) {
        XhwSetting setting = settingRepository.findFirstByK("virtual_num");
        model.addAttribute("virtualNum", setting.getV());
        return "admin/virtual";
    }


    @LoginRequired()
    @PostMapping("/virtual")
    @ResponseBody
    public Map virtualEdit(Integer virtualNum) {
        Map resultMap = new HashMap<String, Object>();
        boolean successLoginFlag = false;
        if (virtualNum != null) {
            successLoginFlag = true;
            XhwSetting setting = settingRepository.findFirstByK("virtual_num");
            setting.setV(virtualNum + "");
            settingRepository.save(setting);
        }
        resultMap.put("success", successLoginFlag);
        return resultMap;
    }


    @LoginRequired()
    @GetMapping("/group")
    public String group(Model model, Integer again, String target) {
        return "admin/group";
    }


    @LoginRequired()
    @GetMapping("/group/list")
    @ResponseBody
    public List<XhwGroup> groupList(String title) {
        List<XhwGroup> all;
        if (StringUtils.isNotBlank(title)) {
            all = xhwGroupRepository.findByTitleLike("%" + title + "%");
        } else {
            all = xhwGroupRepository.findAll();
        }
        return all;
    }

    @RequestMapping("/group/saveIndex")
    public String groupSaveIndex(String id) {
        return "admin/groupSaveIndex";
    }


    @PostMapping("/group/insert")
    @ResponseBody
    public Map groupInsert(XhwGroup xhwGroup) {
        Map resultMap = new HashMap<String, Object>();

        xhwGroup.setFinish(0);
        xhwGroup.setRemainNum(xhwGroup.getMaxNum() * 100);
        xhwGroupRepository.save(xhwGroup);
        resultMap.put("success", true);
        return resultMap;
    }

    @PostMapping("/group/remove")
    @ResponseBody
    public String remveTable(String id) {
        if (StringUtils.isNotBlank(id)) {
            xhwGroupRepository.deleteById(id);
        }
        return "success";
    }


    @LoginRequired()
    @GetMapping("/award")
    public String award() {
        return "admin/award";
    }


    @LoginRequired()
    @GetMapping("/award/list")
    @ResponseBody
    public List<XhwAward> awardList(String searchName, String searchStatus) {

        XhwAward xhwAward = new XhwAward();
        xhwAward.setName(searchName);
        if (StringUtils.isNotBlank(searchStatus)) {
            if (searchStatus.equals("READY")) {
                xhwAward.setAwardRunningType(AwardRunningEnum.READY);
            }
            if (searchStatus.equals("RUNNING")) {
                xhwAward.setAwardRunningType(AwardRunningEnum.RUNNING);
            }
            if (searchStatus.equals("FINISH")) {
                xhwAward.setAwardRunningType(AwardRunningEnum.FINISH);
            }
        }


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<XhwAward> xhwAwardExample = Example.of(xhwAward, matcher);
        List<XhwAward> all = xhwAwardRepository.findAll(xhwAwardExample);
        return all;
    }


    @RequestMapping("/award/saveIndex")
    public String awardSaveIndex(String id, Model model) {
        XhwAward xhwAward = new XhwAward();

        String drawStartTimeString = null;
        if (StringUtils.isNotBlank(id)) {
            Optional<XhwAward> byId = xhwAwardRepository.findById(id);
            if (byId.isPresent()) {
                xhwAward = byId.get();
            }
            drawStartTimeString = CommonDateParseUtil.date2string(xhwAward.getDrawStartTime(), "yyyy-MM-dd HH:mm");
        }

        model.addAttribute("currentAward", xhwAward);
        model.addAttribute("drawStartTimeString", drawStartTimeString);
        return "admin/awardSaveIndex";
    }


    @PostMapping("/award/insert")
    @ResponseBody
    public Map awardInsert(XhwAward xhwAward) {
        Map resultMap = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(xhwAward.getId())) {
            Optional<XhwAward> byId = xhwAwardRepository.findById(xhwAward.getId());

            if (byId.isPresent()) {
                XhwAward db = byId.get();
                db.setName(xhwAward.getName());
                db.setImg(xhwAward.getImg());
                db.setDrawStartTime(xhwAward.getDrawStartTime());
                db.setShowNum(xhwAward.getShowNum());
                db.setTotalNum(xhwAward.getTotalNum());
                db.setLevel(xhwAward.getLevel());
                db.setRemainNum(xhwAward.getTotalNum());
                xhwAwardRepository.save(db);
            }

        } else {
            xhwAward.setAwardRunningType(AwardRunningEnum.READY);
            xhwAward.setSendNum(0);
            xhwAward.setRemainNum(xhwAward.getTotalNum());
            xhwAwardRepository.save(xhwAward);
        }
        resultMap.put("success", true);
        return resultMap;
    }


    @PostMapping("/award/remove")
    @ResponseBody
    public String remveaward(String id) {
        if (StringUtils.isNotBlank(id)) {
            xhwAwardRepository.deleteById(id);
        }
        return "success";
    }

    @PostMapping("/award/launch")
    @ResponseBody
    public String launchaward(String id) {
        Optional<XhwAward> byId = xhwAwardRepository.findById(id);

        if (byId.isPresent()) {
            XhwAward xhwAward = byId.get();
            xhwAward.setAwardRunningType(AwardRunningEnum.RUNNING);
            xhwAwardRepository.save(xhwAward);
        }
        return "success";
    }


    @PostMapping("/award/finish")
    @ResponseBody
    public String finishaward(String id) {
        Optional<XhwAward> byId = xhwAwardRepository.findById(id);

        if (byId.isPresent()) {
            XhwAward xhwAward = byId.get();
            xhwAward.setAwardRunningType(AwardRunningEnum.FINISH);
            xhwAwardRepository.save(xhwAward);
        }
        return "success";
    }


}
