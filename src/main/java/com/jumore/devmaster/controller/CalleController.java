package com.jumore.devmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jumore.devmaster.service.TestService;
import com.jumore.dove.aop.annotation.PublicMethod;
import com.jumore.dove.controller.base.BaseController;
import com.jumore.dove.tranction.dist.DisTransactionCalleManager;

@PublicMethod
@Controller
@RequestMapping(value = "/test")
public class CalleController extends BaseController {

    @Autowired
    private TestService testService;
    
    @ResponseBody
    @RequestMapping(value = "/addProject")
    public Object addProject() throws Exception {
        // 异步执行，本身这次http请求立即返回,addProject的返回结果实际由DisTransactionCalleManager通过redis共享给调用者
        DisTransactionCalleManager.start(new Runnable(){

            @Override
            public void run() {
                testService.addProject();
            }
            
        });
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/addProjectMember")
    public Object addProjectMember() throws Exception {
        DisTransactionCalleManager.start(new Runnable(){

            @Override
            public void run() {
                testService.addProjectMember();
            }
            
        });
        return "";
    }
}
