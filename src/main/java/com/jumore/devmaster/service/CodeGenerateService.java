package com.jumore.devmaster.service;

import com.jumore.devmaster.entity.DBEntity;

public interface CodeGenerateService {

    public String generate(DBEntity table , String className);
}
