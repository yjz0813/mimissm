package com.bjpowernode.controller;

import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class ServiceAction {
    @Autowired
    AdminService adminService;
    @RequestMapping(value = "/login")
    public String login(String name, String pwd ,HttpServletRequest req){
        Admin admin=adminService.login(name,pwd);
        if(admin!=null){
            req.setAttribute("admin",admin);
            return "main";
        }else{
            req.setAttribute("errmsg","密码或用户名错误");
            return "login";
        }
    }
}
