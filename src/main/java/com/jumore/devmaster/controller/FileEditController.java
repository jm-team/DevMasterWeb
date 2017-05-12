package com.jumore.devmaster.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jumore.devmaster.common.CodeMirrorModeContainer;
import com.jumore.devmaster.common.TreeIconClassContainer;
import com.jumore.devmaster.common.util.PathUtils;
import com.jumore.devmaster.common.util.ZipUtil;
import com.jumore.devmaster.entity.ProjectTemplate;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/file")
public class FileEditController {

    @Autowired
    private BaseService baseService;

    @RequestMapping("/fileEdit")
    public ModelAndView index(String root, String type) throws IOException {
        ModelAndView mv = new ModelAndView();
        String dirStr = getRootDir(type) + root;
        File dir = new File(dirStr);

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        }
        mv.addObject("root", root);
        mv.addObject("type", type);
        return mv;
    }

    /**
     * 
     * @author Administrator.
     * @date 2017年3月23日 上午10:30:39
     * @param id 要展开文件夹的相对路径
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "getFiles")
    public JSONArray getFiles(String root, String id, String type) throws Exception {
        String dirStr = getRootDir(type);
        JSONArray arr = new JSONArray();

        if (!StringUtils.isEmpty(id)) {
            dirStr += id;
        } else {
            File dir = new File(dirStr);
            JSONObject jobj = new JSONObject();
            ProjectTemplate tpl = baseService.get(ProjectTemplate.class, Long.valueOf(root));
            jobj.put("text", tpl.getTitle());
            jobj.put("id", root);
            jobj.put("state", "closed");
            jobj.put("folder", true);
            jobj.put("iconCls", TreeIconClassContainer.getIconClass(dir));
            arr.add(jobj);
            return arr;
        }

        File dir = new File(dirStr);
        File[] files = dir.listFiles();

        for (File file : files) {
            JSONObject obj = new JSONObject();
            obj.put("text", file.getName());
            obj.put("folder", file.isDirectory());
            obj.put("id", getRelativePath(file, type));
            obj.put("iconCls", TreeIconClassContainer.getIconClass(file));

            if (file.isDirectory()) {
                obj.put("state", "closed");
            } else {
                obj.put("state", "open");
            }

            arr.add(obj);
        }

        return arr;
    }

    @ResponseBody
    @RequestMapping(value = "addFile")
    public ResponseVo<String> addFile(String parent, String newFileName, boolean isFile, String type) throws Exception {
        String absolutePath = getRootDir(type) + parent + File.separator + newFileName;
        File newFile = new File(absolutePath);

        if (newFile.exists()) {
            return ResponseVo.<String>BUILDER().setDesc("文件名重复").setCode(Const.BUSINESS_CODE.FAILED);
        }

        try {
            if (isFile) {
                newFile.createNewFile();
            } else {
                FileUtils.forceMkdir(newFile);
            }
        } catch (Exception ex) {
            return ResponseVo.<String>BUILDER().setDesc(ex.getMessage()).setCode(Const.BUSINESS_CODE.FAILED);
        }

        return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "deleteFile")
    public ResponseVo<String> deleteFile(String fileName, String type) throws Exception {
        File file = new File(getRootDir(type) + fileName);
        FileUtils.deleteQuietly(file);
        return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "unzip")
    public ResponseVo<String> unzip(String fileName, String type) throws Exception {
        File file = new File(getRootDir(type) + fileName);
        ZipUtil.unzip(getRootDir(type) + fileName, file.getParent());
        return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @ResponseBody
    @RequestMapping("/rename")
    public ResponseVo<String> rename(String oldXPath, String newName, String type) {
        String absolutePath = getRootDir(type) + oldXPath;
        File file = new File(absolutePath);

        int index = file.getAbsolutePath().lastIndexOf(File.separator);
        String newFileName = index == -1 ? newName : file.getAbsolutePath().substring(0, index) + File.separator + newName;
        File newFile = new File(newFileName);

        boolean success = file.renameTo(newFile);

        return ResponseVo.<String>BUILDER().setCode(success ? Const.BUSINESS_CODE.SUCCESS : Const.BUSINESS_CODE.FAILED)
                .setData(getRelativePath(newFile, type));
    }

    @RequestMapping("/codemirror")
    public ModelAndView codemirror(String path, String type) throws IOException {
        ModelAndView mv = new ModelAndView();

        if (org.apache.commons.lang.StringUtils.isNotBlank(path)) {
            String filePath = getRootDir(type) + path;
            File file = new File(filePath);
            if (!file.exists()) {
                FileUtils.forceMkdir(file.getParentFile());
                file.createNewFile();
            }
            String content = FileUtils.readFileToString(file);

            if (!StringUtils.isEmpty(content)) {
                content = content.replaceAll("</textarea>", "&lt;/textarea&gt;");
            }

            mv.addObject("content", content);
            mv.addObject("path", path);
            mv.addObject("type", type);
            mv.addObject("fileName", file.getName());
            mv.addObject("mode", CodeMirrorModeContainer.get(file));
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "save")
    public ResponseVo<String> save(String path, String content, String type) {
        try {
            path = getRootDir(type) + path;
            File file = new File(path);
            FileUtils.write(file, content);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.FAILED);
        }

        return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    @RequestMapping(value = "/picture")
    public void readPicture(String path, HttpServletResponse response, String type) throws IOException {
        path = getRootDir(type) + path;
        File file = new File(path);

        response.setHeader("Content-Type", "image/jpeg");
        response.getOutputStream().write(FileUtils.readFileToByteArray(file));
        response.flushBuffer();
    }

    private String getRelativePath(File file, String type) {
        if (file == null) {
            return "";
        }

        return file.getAbsolutePath().replace(getRootDir(type), "").replace('\\', '/');
    }

    @ResponseBody
    @RequestMapping(value = "/upload")
    public ResponseVo<String> upload(@RequestParam(value = "uploadFile", required = false) MultipartFile file, String parent, String type)
            throws IOException {
        if (StringUtils.isEmpty(parent) || file == null) {
            throw new BusinessException("parent directory cannot be empty or file cannot be null");
        }

        String path = getRootDir(type) + parent;
        File dir = new File(path);

        if (!dir.exists() || !dir.isDirectory()) {
            throw new BusinessException("parent directory is not exists or is not a directory");
        }

        File localFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());

        if (localFile.exists()) {
            throw new BusinessException("the file " + file.getOriginalFilename() + " is already exists");
        }

        localFile.createNewFile();
        FileUtils.writeByteArrayToFile(localFile, file.getBytes());

        return ResponseVo.<String>BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    private String getRootDir(String type) {
        if ("tpls".equals(type)) {
            return PathUtils.getTplDir();
        } else if ("comps".equals(type)) {
            return PathUtils.getComponentsDir();
        }
        return "";
    }
}
