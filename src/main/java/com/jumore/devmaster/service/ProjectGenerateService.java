package com.jumore.devmaster.service;

import com.jumore.devmaster.entity.Project;

public interface ProjectGenerateService {

    public void generateCode(Project project, String tplPath , String codePath);
}
