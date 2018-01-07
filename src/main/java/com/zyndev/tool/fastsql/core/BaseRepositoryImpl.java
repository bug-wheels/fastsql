/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 zyndev zyndev@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.zyndev.tool.fastsql.core;

import com.zyndev.tool.fastsql.util.BeanReflectionUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Base repository.
 *
 * @author: 张瑀楠 zyndev@gmail.com
 */
public class BaseRepositoryImpl implements BaseRepository {

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Object entity) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            System.out.println("tableName:" + tableName);
            StringBuffer property = new StringBuffer();
            StringBuffer value = new StringBuffer();
            List<Object> propertyValue = new ArrayList<Object>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);

            for(DBColumnInfo dbColumnInfo : dbColumnInfos){
                // id 暂时不处理ID
                if(dbColumnInfo.isId()){
                    continue;
                }
                // 不为null
                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if(o!=null){
                    property.append(",").append(dbColumnInfo.getColumnName());
                    value.append(",").append("?");
                    propertyValue.add(o);
                }
            }

            System.out.println("property:" + property);
            System.out.println("value:" + value);
            System.out.println("propertyValue:" + propertyValue);

            String sql = "insert into "+tableName+ "("+property.toString().substring(1)+") values("+value.toString().substring(1)+")";
            System.out.println(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Object entity) {
        return update(entity, true);
    }

    @Override
    public int update(Object entity, boolean ignoreNull) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            StringBuffer property = new StringBuffer();
            StringBuffer where = new StringBuffer();
            List<Object> propertyValue = new ArrayList<Object>();
            List<Object> wherePropertyValue = new ArrayList<Object>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for(DBColumnInfo dbColumnInfo : dbColumnInfos){

                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if(dbColumnInfo.isId()){
                    where.append(" and ");
                    where.append(dbColumnInfo.getColumnName()+" = ? ");
                    wherePropertyValue.add(o);
                } else if(ignoreNull || o != null){
                    property.append(",").append(dbColumnInfo.getColumnName()).append("=?");
                    propertyValue.add(o);
                }
            }

            System.out.println("property:" + property);
            System.out.println("where:" + where);
            System.out.println("propertyValue:" + propertyValue);
            System.out.println("wherePropertyValue:" + wherePropertyValue);

            String sql = "update "+tableName+ " set " +property.toString().substring(1) +" where "+ where.toString().substring(5);
            System.out.println(sql);
            /*// log.info("update: "+sql);
			// log.info("vaule: "+StringUtil.getArrayValue(propertyValue.toArray()));*/

            // return this.getJdbcTemplate().update(sql, propertyValue.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            /*// log.error(StringUtil.outputException(e));*/
        }
        return 0;
    }

    @Override
    public int delete(Object entity) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            StringBuffer where = new StringBuffer(" 1=1 ");
            List<Object> whereValue = new ArrayList<>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for(DBColumnInfo dbColumnInfo : dbColumnInfos){
                if (dbColumnInfo.isId()) {
                    Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                    if (null != o) {
                        whereValue.add(o);
                    }
                    where.append(" and `").append( dbColumnInfo.getColumnName()+"` = ? ");
                }
            }

            if (whereValue.size() == 0) {
                throw new RuntimeException("delete 时 id 无对应值，不能删除");
            }
            String sql = "delete from  "+tableName+ " where "+ where.toString();
            System.out.println(sql);
            System.out.println("whereValue:" + whereValue);
            /*// log.info("delete: "+sql);*/
            // return this.getJdbcTemplate().update(sql);

        } catch (Exception e) {
            e.printStackTrace();
            //// log.error(StringUtil.outputException(e));
        }
        return 0;
    }

    @Override
    public <E> E findById(E entity) {
        return findById(entity, null);
    }

    @Override
    public <E> E findById(E entity, String columns) {
        E result  =  null;
        boolean isExist = false;
        try {
            result = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
            Object tableName = AnnotationParser.getTableName(entity);
            StringBuffer where = new StringBuffer(100);
            List<Object> whereValue = new ArrayList<>();
            where.append(" 1=1 ");
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for(DBColumnInfo dbColumnInfo : dbColumnInfos ){
                if (dbColumnInfo.isId()) {
                    where.append(" and ").append( dbColumnInfo.getColumnName()+"= ? ");
                    Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getColumnName());
                    if (null == o) {
                        throw new RuntimeException("根据ID查询，where 条件为空");
                    }
                    whereValue.add(o);

                }
            }
            String sql = null;
            if(columns == null || columns.length()==0){
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + " from  "+tableName+ " where "+ where.toString();
            }else{
                sql = "select "+columns+"  from  "+tableName+ " where "+ where.toString();
            }
            //// log.info("getObjectById: "+sql);

            SqlRowSet resultSet = this.jdbcTemplate.queryForRowSet(sql);
            Field[] fields = entity.getClass().getDeclaredFields();
            Map<String,String> map = new HashMap<String,String>();
            if(columns!=null){
                String[] tempStrings = columns.split(",");
                for(String str: tempStrings){
                    map.put(str.trim(), str.trim());
                }
            }else{
                for(DBColumnInfo dbColumnInfo : dbColumnInfos){
                    map.put(dbColumnInfo.getColumnName(), dbColumnInfo.getColumnName());
                }
            }
            while(resultSet.next()){
                isExist = true;
                for(Field field : fields){
                    //表字段存在才有意义
                    if(map.get(field.getName())!=null){
                        field.setAccessible(true);
                        field.set(result, resultSet.getObject(field.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //// log.error(StringUtil.outputException(e));
        }
        if(isExist){
            return result;
        }
        return null;
    }

    @Override
    public <E> List<E> getEntityList(E entity) {
        List<E> result = new ArrayList<>();
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            Object tableName = AnnotationParser.getTableName(entity);
            StringBuilder where = new StringBuilder();
            List<Object> propertyValue = new ArrayList<Object>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for(DBColumnInfo dbColumnInfo : dbColumnInfos){
                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getColumnName());
                if (o != null && !"".equals(o.toString())) {
                    where.append(" and ").append(dbColumnInfo.getColumnName()).append(" =?");
                    propertyValue.add(o);
                }
            }
            String sql =  null;
            SqlRowSet resultSet  = null;

            //带条件的查询
            if(propertyValue.size()>0){
                sql = "select "+ AnnotationParser.getTableAllColumn(entity) +"  from  "+tableName+ " where "+ where.toString().substring(4);
            } else{
                sql = "select "+ AnnotationParser.getTableAllColumn(entity) +"   from  "+tableName;
            }


            if(propertyValue.size()>0){
                resultSet  =   this.jdbcTemplate.queryForRowSet(sql,propertyValue.toArray());
                // log.info("getObjectById: "+sql);
            } else{
                resultSet  =   this.jdbcTemplate.queryForRowSet(sql);
                // log.info("getObjectById: "+sql);
            }

            Map<String,String> map = new HashMap<String,String>();
            for(DBColumnInfo dbColumnInfo : dbColumnInfos ){
                map.put(dbColumnInfo.getColumnName(), dbColumnInfo.getColumnName());
            }

            while(resultSet.next()){
                E temp = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for(Field field : fields){
                    if(map.get(field.getName())!=null){
                        field.setAccessible(true);
                        field.set(temp, resultSet.getObject(field.getName()));
                    }
                }
                result.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
