package com.jumore.devmaster.service;

import com.jumore.devmaster.entity.Project;
import com.jumore.dove.service.BaseService;

/**
 * Created by Administrator on 2017/3/31.
 */
public interface ProjectService extends BaseService{
    /**
     * 同步库表之添加表与列数据
     *
     * @param project Project
     * @param driverClass 驱动
     */
    boolean addTableAndColumnInfo(Project project, String driverClass);
}
