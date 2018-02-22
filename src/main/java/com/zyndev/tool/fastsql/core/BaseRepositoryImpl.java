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
import org.apache.commons.lang3.StringUtils;
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
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
public class BaseRepositoryImpl implements BaseRepository {

    /**
     * Save int.
     *
     * @param entity the entity
     * @return the int
     */
    @Override
    public int save(Object entity) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            StringBuilder property = new StringBuilder();
            StringBuilder value = new StringBuilder();
            List<Object> propertyValue = new ArrayList<>();
            List<DBColumnInfo> dbColumnInfoList = AnnotationParser.getAllDBColumnInfo(entity);

            for (DBColumnInfo dbColumnInfo : dbColumnInfoList) {
                if (dbColumnInfo.isId() || !dbColumnInfo.isInsertAble()) {
                    continue;
                }
                // 不为null
                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if (o != null) {
                    property.append(",").append(dbColumnInfo.getColumnName());
                    value.append(",").append("?");
                    propertyValue.add(o);
                }
            }
            String sql = "insert into " + tableName + "(" + property.toString().substring(1) + ") values(" + value.toString().substring(1) + ")";
            return this.getJdbcTemplate().update(sql, propertyValue.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Update int.
     *
     * @param entity the entity
     * @return the int
     */
    @Override
    public int update(Object entity) {
        return update(entity, true, null);
    }

    /**
     * Update int.
     *
     * @param entity  the entity
     * @param columns the columns
     * @return the int
     */
    @Override
    public int update(Object entity, String... columns) {
        return update(entity, true, columns);
    }

    /**
     * Update int.
     *
     * @param entity     the entity
     * @param ignoreNull the ignore null
     * @return the int
     */
    @Override
    public int update(Object entity, boolean ignoreNull) {
        return update(entity, ignoreNull, null);
    }

    /**
     * Update int.
     *
     * @param entity     the entity
     * @param ignoreNull the ignore null
     * @param columns    the columns
     * @return the int
     */
    @Override
    public int update(Object entity, boolean ignoreNull, String... columns) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            StringBuilder property = new StringBuilder();
            StringBuilder where = new StringBuilder();
            List<Object> propertyValue = new ArrayList<>();
            List<Object> wherePropertyValue = new ArrayList<>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {

                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if (dbColumnInfo.isId()) {
                    where.append(" and ").append(dbColumnInfo.getColumnName()).append(" = ? ");
                    wherePropertyValue.add(o);
                } else if (ignoreNull || o != null) {
                    property.append(",").append(dbColumnInfo.getColumnName()).append("=?");
                    propertyValue.add(o);
                }
            }

            if (wherePropertyValue.isEmpty()) {
                throw new IllegalArgumentException("更新表 [" + tableName + "] 无法找到id, 请求数据：" + entity);
            }

            String sql = "update " + tableName + " set " + property.toString().substring(1) + " where " + where.toString().substring(5);
            propertyValue.addAll(wherePropertyValue);
            return this.getJdbcTemplate().update(sql, propertyValue.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Delete int.
     * <p>根据id 删除对应的数据</p>
     *
     * @param entity the entity
     * @return the int
     */
    @Override
    public int delete(Object entity) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            StringBuilder where = new StringBuilder(" 1=1 ");
            List<Object> whereValue = new ArrayList<>(5);
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                if (dbColumnInfo.isId()) {
                    Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                    if (null != o) {
                        whereValue.add(o);
                    }
                    where.append(" and `").append(dbColumnInfo.getColumnName()).append("` = ? ");
                }
            }

            if (whereValue.size() == 0) {
                throw new IllegalStateException("delete " + tableName + " id 无对应值，不能删除");
            }
            String sql = "delete from  " + tableName + " where " + where.toString();
            return this.getJdbcTemplate().update(sql, whereValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Find by id e.
     *
     * @param entity the entity
     * @return the e
     */
    @Override
    public <E> E findById(E entity) {
        return findById(entity, null);
    }

    /**
     * Find by id e.
     *
     * @param entity  the entity
     * @param columns the columns
     * @return the e
     */
    @Override
    public <E> E findById(E entity, String... columns) {
        E result = null;
        boolean isExist = false;
        try {
            //noinspection unchecked
            result = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
            Object tableName = AnnotationParser.getTableName(entity);
            StringBuilder where = new StringBuilder(100);
            List<Object> whereValue = new ArrayList<>();
            where.append(" 1=1 ");
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                if (dbColumnInfo.isId()) {
                    where.append(" and ").append(dbColumnInfo.getColumnName()).append("= ? ");
                    Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getColumnName());
                    if (null == o) {
                        throw new RuntimeException("根据ID查询，where 条件为空");
                    }
                    whereValue.add(o);

                }
            }
            String sql = null;
            if (columns == null || columns.length == 0) {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + " from  " + tableName + " where " + where.toString();
            } else {
                sql = "select " + columns + "  from  " + tableName + " where " + where.toString();
            }
            //// log.info("getObjectById: "+sql);

            SqlRowSet resultSet = this.getJdbcTemplate().queryForRowSet(sql, whereValue);
            Field[] fields = entity.getClass().getDeclaredFields();
            Map<String, String> map = new HashMap<>();
            if (columns != null) {
                for (String str : columns) {
                    map.put(str.trim(), str.trim());
                }
            } else {
                for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                    map.put(dbColumnInfo.getColumnName(), dbColumnInfo.getColumnName());
                }
            }
            while (resultSet.next()) {
                isExist = true;
                for (Field field : fields) {
                    //表字段存在才有意义
                    if (map.get(field.getName()) != null) {
                        field.setAccessible(true);
                        field.set(result, resultSet.getObject(field.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isExist) {
            return result;
        }
        return null;
    }

    /**
     * Gets entity list.
     *
     * @param entity the entity
     * @return the entity list
     */
    @Override
    public <E> List<E> getEntityList(E entity) {
        return getEntityList(entity, null);
    }

    /**
     * Gets entity list.
     *
     * @param entity  the entity
     * @param columns the columns
     * @return the entity list
     */
    @Override
    public <E> List<E> getEntityList(E entity, String... columns) {
        List<E> result = new ArrayList<>();
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            Object tableName = AnnotationParser.getTableName(entity);
            StringBuilder where = new StringBuilder();
            List<Object> propertyValue = new ArrayList<>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getColumnName());
                if (o != null && !"".equals(o.toString())) {
                    where.append(" and ").append(dbColumnInfo.getColumnName()).append(" =?");
                    propertyValue.add(o);
                }
            }
            String sql = null;

            //带条件的查询
            if (propertyValue.size() > 0) {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + " from  " + tableName + " where " + where.toString().substring(4);
            } else {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + "  from  " + tableName;
            }

            SqlRowSet resultSet = this.getJdbcTemplate().queryForRowSet(sql, propertyValue.toArray());

            Map<String, String> map = new HashMap<String, String>();
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                map.put(dbColumnInfo.getColumnName(), dbColumnInfo.getColumnName());
            }

            while (resultSet.next()) {
                @SuppressWarnings("unchecked")
                E temp = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for (Field field : fields) {
                    if (map.get(field.getName()) != null) {
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

    /**
     * Gets entity list.
     *
     * @param sql    the sql
     * @param entity the entity
     * @return the entity list
     */
    @Override
    public <E> List<E> getEntityList(String sql, E entity) {
        return getEntityList(sql, null, entity);
    }

    /**
     * Gets entity list.
     *
     * @param sql    the sql
     * @param args   the args
     * @param entity the entity
     * @return the entity list
     */
    @Override
    public <E> List<E> getEntityList(String sql, Object[] args, E entity) {
        List<E> list = new ArrayList<>();
        try {
            SqlRowSet result = this.getJdbcTemplate().queryForRowSet(sql, args);
            Map<String, String> map = new HashMap<>();
            //obj 获得字段
            Field[] fields = BeanReflectionUtil.getBeanDeclaredFields(entity.getClass().getName());
            while (result.next()) {
                @SuppressWarnings("unchecked")
                E temp = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for (Field field : fields) {
                    if (map.get(field.getName()) != null) {
                        field.setAccessible(true);
                        field.set(temp, result.getObject(field.getName()));
                    }
                }
                list.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * Gets entity page list.
     *
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return the entity page list
     */
    @Override
    public <E> PageListContent<E> getEntityPageList(E entity, int pageNum, int pageSize) {
        return getEntityPageList(entity, pageNum, pageSize, null, null);
    }

    @Override
    public <E> PageListContent<E> getEntityPageList(E entity, int pageNum, int pageSize, String orderBy) {
        return getEntityPageList(entity, pageNum, pageSize, null, null);
    }

    /**
     * Gets entity page list.
     *
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param columns  the columns
     * @return the entity page list
     */
    @Override
    public <E> PageListContent<E> getEntityPageList(E entity, int pageNum, int pageSize, String orderBy, String... columns) {
        try {
            Field[] fields = BeanReflectionUtil.getBeanDeclaredFields(entity.getClass().getName());
            String tableName = AnnotationParser.getTableName(entity);
            StringBuffer where = new StringBuffer();
            List<Object> propertyValue = new ArrayList<Object>();
            List<DBColumnInfo> dbColumnInfoList = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo vo : dbColumnInfoList) {
                Object o = BeanReflectionUtil.getPrivatePropertyValue(entity, vo.getColumnName());
                if (o != null && !o.toString().equals("")) {
                    where.append(" and ").append(vo.getColumnName()).append(" =?");
                    propertyValue.add(o);
                }
            }
            String sql;
            SqlRowSet result = null;
            //带条件的查询
            if (propertyValue.size() > 0) {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + "  from  " + tableName + " where " + where.toString().substring(4);
            } else {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + "   from  " + tableName;
            }

            PageListContent<E> pageListContent = new PageListContent<>();

            pageListContent.setPageNum(pageNum);
            pageListContent.setPageSize(pageSize);

            int pageTotal = this.getJdbcTemplate().queryForObject(sql.replaceAll("(?i)select([\\s\\S]*?)from", "select count(*) from "), propertyValue.toArray(), Integer.class);
            pageListContent.setTotalNum(pageTotal);

            if (pageTotal == 0) {
                pageListContent.setContent(new ArrayList<E>(0));
                return pageListContent;
            }

            sql = sql + " limit " + pageListContent.getOffset() + "," + pageListContent.getPageSize();

            if (propertyValue.size() > 0) {
                result = this.getJdbcTemplate().queryForRowSet(sql, propertyValue.toArray());
            } else {
                result = this.getJdbcTemplate().queryForRowSet(sql);
            }

            Map<String, String> map = new HashMap<String, String>();

            for (DBColumnInfo vo : dbColumnInfoList) {
                map.put(vo.getColumnName(), vo.getColumnName());
            }

            List<E> list = new ArrayList<>();

            while (result.next()) {
                E temp = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for (Field field : fields) {
                    if (map.get(field.getName()) != null) {
                        field.setAccessible(true);
                        field.set(temp, result.getObject(field.getName()));
                    }
                }
                list.add(temp);
            }
            pageListContent.setContent(list);
            return pageListContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets entity page list by sql.
     *
     * @param sql      the sql
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return the entity page list by sql
     */
    @Override
    public <E> PageListContent<E> getEntityPageListBySql(String sql, E entity, int pageNum, int pageSize) {
        return getEntityPageListBySql(sql, null, entity, pageNum, pageSize, null);
    }

    /**
     * Gets entity page list by sql.
     *
     * @param sql      the sql
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param orderBy  the order by
     * @return the entity page list by sql
     */
    @Override
    public <E> PageListContent<E> getEntityPageListBySql(String sql, E entity, int pageNum, int pageSize, String orderBy) {
        return getEntityPageListBySql(sql, null, entity, pageNum, pageSize, orderBy);
    }

    /**
     * Gets entity page list by sql.
     *
     * @param sql      the sql
     * @param args     the args
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return the entity page list by sql
     */
    @Override
    public <E> PageListContent<E> getEntityPageListBySql(String sql, Object[] args, E entity, int pageNum, int pageSize, String orderBy) {
        try {
            PageListContent<E> pageListContent = new PageListContent<>();
            pageListContent.setPageNum(pageNum);
            pageListContent.setPageSize(pageSize);
            int pageTotal = this.getJdbcTemplate().queryForObject(sql.replaceAll("(?i)select([\\s\\S]*?)from", "select count(*) from "), propertyValue.toArray(), Integer.class);
            pageListContent.setTotalNum(pageTotal);

            if (pageTotal == 0) {
                pageListContent.setContent(new ArrayList<E>(0));
                return pageListContent;
            }

            if (StringUtils.isNotBlank(orderBy)) {
                sql = sql + " order by " + orderBy;
            }

            sql = sql + " limit " + pageListContent.getOffset() + "," + pageListContent.getPageSize();

            SqlRowSet sqlRowSet = this.getJdbcTemplate().queryForRowSet(sql, args);

            Map<String, String> map = new HashMap<>();
            List<DBColumnInfo> dbColumnInfoList = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo vo : dbColumnInfoList) {
                map.put(vo.getColumnName(), vo.getColumnName());
            }

            List<E> list = new ArrayList<>();
            Field[] declaredFields = entity.getClass().getDeclaredFields();
            while (sqlRowSet.next()) {
                E temp = (E) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for (Field field : declaredFields) {
                    if (map.get(field.getName()) != null) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        field.set(temp, sqlRowSet.getObject(field.getName()));
                    }
                }
                list.add(temp);
            }
            pageListContent.setContent(list);
            return pageListContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JdbcTemplate getJdbcTemplate() {
        return DataSourceHolder.getInstance().getJdbcTemplate();
    }

}
