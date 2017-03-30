package com.jumore.devmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jumore.devmaster.entity.EntityField;
import com.jumore.dove.plugin.Page;
import com.jumore.dove.service.BaseService;
import com.jumore.dove.util.ParamMap;
import com.jumore.dove.web.model.Const;
import com.jumore.dove.web.model.ResponseVo;

@Controller
@RequestMapping(value = "/field")
public class FieldController {
    
    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/fieldList")
    public ModelAndView fieldList(Long projectId , Long entityId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        mv.addObject("entityId", entityId);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "listFieldData")
    public ResponseVo<Page<EntityField>> listFieldData(Page<EntityField> page,Long projectId , Long entityId, String name) throws Exception {
        ParamMap pm = new ParamMap();
        pm.put("projectId", projectId);
        pm.put("entityId", entityId);
        pm.put("name", name);
        page = baseService.findPageByParams(EntityField.class , page, "Field.listField", pm);
        return ResponseVo.<Page<EntityField>> BUILDER().setData(page).setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/addField")
    public ModelAndView addField(Long projectId , Long entityId) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectId", projectId);
        mv.addObject("entityId", entityId);
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value = "doAddField")
    public ResponseVo<Page<String>> doAddField(EntityField field) throws Exception {
        //validate
    	validateEntityField(field);
    	checkNameDuplicate(field);
    	
        baseService.save(field);
        return ResponseVo.<Page<String>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }

    // 检查字段名重复
	private void checkNameDuplicate(EntityField field) {
		EntityField example = new EntityField();
    	example.setName(field.getName());
    	example.setProjectId(field.getProjectId());
    	example.setDbentityId(field.getDbentityId());
    	List<EntityField> list = baseService.listByExample(example);
    	if (list != null && list.size() > 0) {
    		throw new RuntimeException("字段名已存在");
    	}
	}
	
	private void validateEntityField(EntityField field) {
		if (StringUtils.isEmpty(field.getName())) {
            throw new RuntimeException("字段名不能为空");
        }
	}
    
    @ResponseBody
    @RequestMapping(value = "delete")
    public ResponseVo<Page<String>> deleteField(Long id) throws Exception {
    	EntityField field = baseService.get(EntityField.class, id);
        if (field == null) {
            throw new RuntimeException("字段不存在或已删除");
        }
        baseService.delete(field);
        return ResponseVo.<Page<String>> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @RequestMapping(value = "/editField")
    public ModelAndView editField(Long id) throws Exception {
        ModelAndView mv = new ModelAndView();
        EntityField field = baseService.get(EntityField.class, id);
        mv.addObject("field", field);
        return mv;
    }
    
    @ResponseBody
    @RequestMapping(value = "/doUpdateField")
    public ResponseVo<String> doUpdateField(EntityField field) throws Exception {
    	validateEntityField(field);
        EntityField updateField = baseService.get(EntityField.class, field.getId());
        if (!updateField.getName().equals(field.getName())) { // 如果修改了字段名，要检查字段名重复
        	checkNameDuplicate(field);
        }
        updateField.setName(field.getName());
        updateField.setType(field.getType());
        updateField.setLength(field.getLength());
        updateField.setDefaultValue(field.getDefaultValue());
        updateField.setDocs(field.getDocs());
        updateField.setPrimaryKey(field.getPrimaryKey());
        updateField.setAllowNull(field.getAllowNull());
        baseService.update(updateField);
        return ResponseVo.<String> BUILDER().setData("").setCode(Const.BUSINESS_CODE.SUCCESS);
    }
    
    @ResponseBody
    @RequestMapping(value = "/setShowInput")
    public ResponseVo<String> setShowInput(Long id , Integer showInput) throws Exception {
        EntityField po = baseService.get(EntityField.class, id);
        po.setShowInput(showInput);
        baseService.update(po);
        return ResponseVo.<String> BUILDER().setCode(Const.BUSINESS_CODE.SUCCESS);
    }
}
