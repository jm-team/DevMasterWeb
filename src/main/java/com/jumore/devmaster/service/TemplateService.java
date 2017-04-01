package com.jumore.devmaster.service;

import com.jumore.dove.service.BaseService;

public interface TemplateService extends BaseService{
    void cloneTemplate(Long tplId, String newTplTitle) throws Exception;
}
