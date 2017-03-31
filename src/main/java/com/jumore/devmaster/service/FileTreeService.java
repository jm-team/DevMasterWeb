package com.jumore.devmaster.service;

import java.io.File;

import com.alibaba.fastjson.JSONArray;

public interface FileTreeService {

    public JSONArray getFies(File dir);
}
