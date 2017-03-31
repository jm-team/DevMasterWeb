package com.jumore.devmaster.service;

import com.jumore.devmaster.entity.Project;
import com.jumore.dove.service.BaseService;

/**
 * Created by Administrator on 2017/3/31.
 */
public interface ProjectService extends BaseService{
    boolean addTableAndColumnInfo(Project project, String driverClass);
}
