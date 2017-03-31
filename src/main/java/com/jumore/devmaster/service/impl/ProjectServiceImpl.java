package com.jumore.devmaster.service.impl;

import com.jumore.devmaster.common.util.ConnectionUtil;
import com.jumore.devmaster.entity.DBEntity;
import com.jumore.devmaster.entity.EntityField;
import com.jumore.devmaster.entity.Project;
import com.jumore.devmaster.service.ProjectService;
import com.jumore.dove.common.BusinessException;
import com.jumore.dove.service.BaseServiceImpl;
import com.jumore.dove.util.ParamMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {
    /**
     * 添加表与列数据
     *
     * @param project Project
     * @param driverClass 驱动
     */
    @Transactional
    public boolean addTableAndColumnInfo(Project project, String driverClass) {
        ParamMap pm = new ParamMap();
        pm.put("projectId", project.getId());
        // 保存之前先删除老的数据
        execute("Entity.delField", pm);
        execute("Entity.delEntity", pm);

        try {
            Connection connection = ConnectionUtil.initConnection(driverClass, project.getDbUrl(), project.getDbUserName(),
                    project.getDbPassword());
            DatabaseMetaData metadata = connection.getMetaData();
            // 获取表信息
            ResultSet tables = metadata.getTables(null, null, null, null);

            List<DBEntity> dbEntityList = new ArrayList<>();
            DBEntity dbEntity;
            while (tables.next()) {
                dbEntity = new DBEntity();
                dbEntity.setDatabaseName(tables.getString("TABLE_CAT"));
                dbEntity.setName(tables.getString("TABLE_NAME"));// 表名
                dbEntity.setRemark(tables.getString("REMARKS"));// 表备注
                dbEntity.setProjectId(project.getId());
                dbEntityList.add(dbEntity);
            }
            if (!CollectionUtils.isEmpty(dbEntityList)) {
                // 保存表信息
                batchSave(dbEntityList);

                // 获取列信息
                List<EntityField> entityFieldList = getAllColumnInfoList(dbEntityList, metadata, project.getId());

                // 保存列信息
                batchSave(entityFieldList);
            }
            return true;
        } catch (Exception e) {
            throw new BusinessException("同步库表信息失败" , e);
        }
    }


    /**
     * 获取所有表的列信息
     *
     * @param entities
     * @param metadata
     * @param projectId
     * @return
     */
    private List<EntityField> getAllColumnInfoList(List<DBEntity> entities, DatabaseMetaData metadata, Long projectId) throws SQLException {
        List<EntityField> allList = new ArrayList<>();
        for (DBEntity entity : entities) {
            allList.addAll(getColumnInfoList(metadata, entity.getName(), entity.getId(), projectId));
        }
        return allList;
    }

    /**
     * 获取表的列信息
     *
     * @param metadata
     * @param tableName
     * @param entityId
     * @param projectId
     * @return
     */
    private List<EntityField> getColumnInfoList(DatabaseMetaData metadata, String tableName, Long entityId, Long projectId)
            throws SQLException {
        List<EntityField> lst = new ArrayList<>();
        EntityField field;
        ResultSet columns = null;
        columns = metadata.getColumns(null, null, tableName, null);
        List<String> pkList = new ArrayList<String>();
        ResultSet set = metadata.getPrimaryKeys(null, null, tableName);
        while(set.next()){
            pkList.add(set.getString("COLUMN_NAME").toString());
        }
        while (columns.next()) {
            field = new EntityField();
            //{.BUFFER_LENGTH=7, .CHAR_OCTET_LENGTH=15, .COLUMN_DEF=12, .COLUMN_NAME=3, .COLUMN_SIZE=6, .
            //DATA_TYPE=4, .DECIMAL_DIGITS=8, .IS_AUTOINCREMENT=22, .IS_GENERATEDCOLUMN=23, .IS_NULLABLE=17, .NULLABLE=10,
            //.NUM_PREC_RADIX=9, .ORDINAL_POSITION=16, .REMARKS=11, .SCOPE_CATALOG=18, .SCOPE_SCHEMA=19,
            //.SCOPE_TABLE=20, .SOURCE_DATA_TYPE=21, .SQL_DATA_TYPE=13, .SQL_DATETIME_SUB=14, .TABLE_CAT=0, .TABLE_NAME=2, .TABLE_SCHEM=1, .TYPE_NAME=5}
            field.setName(columns.getString("COLUMN_NAME"));
            field.setType(columns.getString("TYPE_NAME"));
            field.setLength(Integer.parseInt(columns.getString("COLUMN_SIZE")));
            field.setDefaultValue(columns.getString("COLUMN_DEF"));
            field.setDocs(columns.getString("REMARKS"));
            field.setAllowNull(columns.getString("IS_NULLABLE"));
            if(pkList.contains(field.getName())){
                field.setPrimaryKey(1);
            }
            field.setDbentityId(entityId);
            field.setProjectId(projectId);
            lst.add(field);
        }

        return lst;
    }
}
