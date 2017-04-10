package com.jumore.devmaster.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.common.DevMasterConst;
import com.jumore.devmaster.common.model.Configuration;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.entity.FrontComponent;
import com.jumore.devmaster.entity.FrontComponentSetting;
import com.jumore.dove.service.BaseService;

/**
 * 做一个简单的web资源服务器
 * 预览组件
 * @author Administrator
 * @date 2017年4月7日 上午10:15:55
 * @version 
 * @see
 */
@Controller
@RequestMapping(value = "/preview")
public class PreviewController {

    @Autowired
    private BaseService   baseService;

    @Autowired
    private Configuration cfg;
    
    @RequestMapping(value = "/**")
    public void res(HttpServletRequest req ,HttpServletResponse resp) throws Exception {
        ModelAndView mv = new ModelAndView();
        String fileName = req.getServletPath().replace("/preview/", "");
        String rootName = fileName.split("/")[0];
        resolveResource(req , PathUtils.getComponentsDir() + fileName , resp , rootName);
    }

    @RequestMapping(value = "/show")
    public ModelAndView show(Long id, HttpServletResponse resp) throws Exception {
        ModelAndView mv = new ModelAndView();
        FrontComponent compPo = baseService.get(FrontComponent.class, id);
        String mainHtml = PathUtils.getComponentsDir() + PathUtils.getRootName(compPo) +File.separator+ compPo.getName()+"."+compPo.getVersion()+"."+compPo.getType();
        File mainFile = new File(mainHtml);
        // 生产一个preview html页面
        // FileUtils.writeStringToFile(new File(PathUtils.getComponentsDir() +
        // getRootName(compPo)+ File.separator +
        // DevMasterConst.Component.PreviewFileName), doc.html(), "utf8");
        // mv.setViewName("forward:"+cfg.getCompPreviewHost()+getRootName(compPo)+"/"+
        // DevMasterConst.Component.PreviewFileName);
        mv.addObject("comp", PathUtils.getRootName(compPo)+"/"+mainFile.getName()+"?id="+id);
        FrontComponentSetting setVo = new FrontComponentSetting();
        setVo.setCompId(id);
        List<FrontComponentSetting> settingList = baseService.listByExample(setVo);
        mv.addObject("settingList", settingList);
//        mv.setViewName("redirect:"+PathUtils.getRootName(compPo)+"/"+mainFile.getName());
        return mv;
    }

    private void resolveResource(HttpServletRequest req ,String file , HttpServletResponse resp , String rootName) throws IOException{
        File mainFile = new File(file);
        String content = FileUtils.readFileToString(mainFile, "utf8");
        ServletOutputStream out = resp.getOutputStream();
        //set content type
        if(file.endsWith(".html")){
            resp.setContentType("text/html;charset=UTF-8");
        }else if(file.endsWith(".js")){
            resp.setContentType("application/x-javascript;charset=UTF-8");
        }else if(file.endsWith(".css")){
            resp.setContentType("text/css;charset=UTF-8");
        }else if(file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".jpeg") || file.endsWith(".jpe") ||file.endsWith(".git") || file.endsWith(".bmp")|| file.endsWith(".ico")){
            resp.setContentType("image/*");
        }
        if(file.endsWith(".html")){
            // 解析html内容
            Document doc = Jsoup.parse(content);
            Elements imports = doc.getElementsByTag(DevMasterConst.Component.ImportTagName);
            for (Element ele : imports) {
                String scop = ele.attr("scope");
                if ("component".equals(scop)) {
                    String src = ele.attr("src");
                    ele.attr("src", cfg.getCompPreviewHost() + rootName + "/" + src);
                }
            }
            // 加在默认参数
            String id = req.getParameter("id");
            FrontComponentSetting settingVo = new FrontComponentSetting();
            settingVo.setCompId(Long.valueOf(id));
            List<FrontComponentSetting> settingList = baseService.listByExample(settingVo);
            String result = doc.html();
            for(FrontComponentSetting setting : settingList){
                if("input".equals(setting.getType())){
                    result = result.replace(setting.getPlaceholder(), setting.getDefaultValue().replace("\"", "&quot;").replace("'", "&quot;"));
                }else if("enum".equals(setting.getType())){
                    String enums = setting.getEnumList();
                    if(StringUtils.isNotBlank(enums)){
                        result = result.replace(setting.getPlaceholder(), enums.split(",")[0]);
                    }
                }
            }
            out.write(result.getBytes());
        }else{
            out.write(content.getBytes());
        }
        
        out.flush();
        out.close();
    }
}
