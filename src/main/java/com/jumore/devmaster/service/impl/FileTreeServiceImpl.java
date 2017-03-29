package com.jumore.devmaster.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.jumore.devmaster.service.FileTreeService;
import com.jumore.dove.service.BaseService;

public class FileTreeServiceImpl implements FileTreeService{

    @Autowired
    private BaseService baseService;
    
    @Override
    public JSONArray getFies(File dir) {
        return null;
    }

}
