package com.jumore.devmaster.controller;

import com.jumore.devmaster.entity.HostConfig;
import com.jumore.devmaster.enums.LoginTypeEnum;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 主机配置
 * Created by coder_long on 2017/4/20.
 */
@Controller
@RequestMapping("/hostConfig")
public class HostConfigController {
    @Resource
    private BaseService baseService;

    @RequestMapping(value = "list")
    public ModelAndView list(){
        return new ModelAndView();
    }

    @ResponseBody
    @RequestMapping(value = "query")
    public ResponseVo<Page<HostConfig>> query(Page<HostConfig> page, HostConfig param) {
        ParamMap pm = new ParamMap(param);
        page = baseService.findPageByParams(HostConfig.class, page, "HostConfig.queryHostConfig", pm);
        return ResponseVo.<Page<HostConfig>>BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "edit")
    public ModelAndView edit(Long id){
        HostConfig hostConfig = null;
        if(id != null && id>0){
            hostConfig = baseService.get(HostConfig.class, id);
        }

        return new ModelAndView()
                .addObject("hostConfig", hostConfig)
                .addObject("loginTypeList", LoginTypeEnum.values());
    }

    @ResponseBody
    @RequestMapping(value = "save")
    public ResponseVo<String> save(HostConfig hostConfig) {
        if(hostConfig.getSu() == null){
            hostConfig.setSu("N");
        }
        if(hostConfig.getSudo() == null){
            hostConfig.setSudo("N");
        }
        if (hostConfig.getId() == null || hostConfig.getId() <= 0) {
            hostConfig.setDeleteFlag(0);
            hostConfig.setCreateTime(new Date());
            baseService.save(hostConfig);
        } else {
            baseService.update(hostConfig);
        }

        return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS).setDesc("保存成功！");
    }

    @ResponseBody
    @RequestMapping(value = "del")
    public ResponseVo<String> del(Long id) {

       if(id == null || id <= 0){
           return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.FAILED).setDesc("参数错误！");
       }

       HostConfig hostConfig = baseService.get(HostConfig.class, id);

       if(hostConfig == null || hostConfig.getDeleteFlag().equals(1)){
           return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.FAILED).setDesc("数据不存在或已删除！");
       }
       hostConfig.setDeleteFlag(1);
       baseService.update(hostConfig);

       return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS).setDesc("删除成功！");
    }
}
