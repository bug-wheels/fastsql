package com.zyndev.tool.fastsql.repository.impl;

import com.zyndev.tool.fastsql.core.AnnotationParser;
import com.zyndev.tool.fastsql.core.DBColumnInfo;
import com.zyndev.tool.fastsql.core.JdbcTemplateHolder;
import com.zyndev.tool.fastsql.core.PageListContent;
import com.zyndev.tool.fastsql.repository.CrudRepository;
import com.zyndev.tool.fastsql.util.BeanReflectionUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
public class CrudRepositoryImpl implements CrudRepository {
    @Override
    public <T> boolean existsById(T entity) {
        return findById(entity, "id") != null;
    }

    @Override
    public <T> long count(T entity) {
        return 0;
    }

    @Override
    public <T> int save(T entity) {
        return save(entity, true);
    }

    @Override
    public <T> int saveAll(Iterable<T> entities) {
        return 0;
    }

    @Override
    public <T> int save(T entity, boolean ignoreNull) {
        try {
            String tableName = AnnotationParser.getTableName(entity);

            StringBuilder property = new StringBuilder();

            StringBuilder value = new StringBuilder();

            List<Object> propertyValue = new ArrayList<>();

            List<DBColumnInfo> dbColumnInfoList = AnnotationParser.getAllDBColumnInfo(entity);

            for (DBColumnInfo dbColumnInfo : dbColumnInfoList) {
                if (dbColumnInfo.isId() || !dbColumnInfo.isInsertable()) {
                    continue;
                }
                // 不为null
                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if (o != null || ignoreNull) {
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

    @Override
    public <T> int saveAll(Iterable<T> entities, boolean ignoreNull) {
        return 0;
    }

    @Override
    public <T> int deleteById(T entity) {
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

    @Override
    public <T> int delete(T entity) {
        try {
            String tableName = AnnotationParser.getTableName(entity);
            StringBuilder where = new StringBuilder(" 1=1 ");
            List<Object> whereValue = new ArrayList<>(5);
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if (null != o) {
                    whereValue.add(o);
                }
                where.append(" and `").append(dbColumnInfo.getColumnName()).append("` = ? ");
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

    @Override
    public <T> int deleteAll(Iterable<T> entities) {

        return 0;
    }

    @Override
    public <T> int update(T entity) {
        return update(entity, true);
    }

    @Override
    public <T> int update(T entity, boolean ignoreNull) {
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

    @Override
    public <T> int update(Iterable<T> entities) {
        return 0;
    }

    @Override
    public <T> int update(Iterable<T> entities, boolean ignoreNull) {
        return 0;
    }

    @Override
    public <T> T findById(T entity) {
        return findById(entity);
    }

    @Override
    public <T> T findById(T entity, String... columns) {
        try {
            Object tableName = AnnotationParser.getTableName(entity);
            StringBuilder where = new StringBuilder(20);
            List<Object> whereValue = new ArrayList<>();
            where.append(" 1=1 ");
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                if (dbColumnInfo.isId()) {
                    where.append(" and ").append(dbColumnInfo.getColumnName()).append("= ? ");
                    Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                    if (null == o) {
                        throw new IllegalArgumentException("根据ID查询，where 条件为空");
                    }
                    whereValue.add(o);
                }
            }

            String sql = null;
            if (columns == null || columns.length == 0) {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + " from  " + tableName + " where " + where.toString();
                columns = AnnotationParser.getTableAllColumn(entity).split(",");
            } else {
                StringBuilder columnsName = new StringBuilder(20);
                for (String str : columns) {
                    columnsName.append(str).append(", ");
                }

                sql = "select " + columnsName.toString().substring(0, columnsName.length() - 2) + "  from  " + tableName + " where " + where.toString();
            }

            System.out.println("执行 sql :" + sql);

            SqlRowSet resultSet = this.getJdbcTemplate().queryForRowSet(sql, whereValue);
            Field[] fields = entity.getClass().getDeclaredFields();

            Set<String> columnSet = new HashSet<>();
            Map<String, String> fieldColumnMap = new HashMap<>();

            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                fieldColumnMap.put(dbColumnInfo.getFieldName(), dbColumnInfo.getColumnName());
            }

            for (String str : columns) {
                columnSet.add(str.trim());
            }
            T result = null;
            if (resultSet.next()) {
                result = (T) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for (Field field : fields) {
                    //表字段存在才有意义
                    if (columnSet.contains(fieldColumnMap.get(field.getName()))) {
                        field.setAccessible(true);
                        field.set(result, resultSet.getObject(fieldColumnMap.get(field.getName())));
                    }
                }
            } else {
                return null;
            }

            if (resultSet.next()) {
                throw new IllegalStateException(" 期望得到 0 或 1 个结果，实际得到多个");
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <T> List<T> getEntityList(T entity) {
        return getEntityList(entity);
    }

    @Override
    public <T> List<T> getEntityList(T entity, String... columns) {
        List<T> result = new ArrayList<>();
        try {
            Object tableName = AnnotationParser.getTableName(entity);

            StringBuilder where = new StringBuilder(30);
            List<Object> propertyValue = new ArrayList<>();
            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);

            Map<String, String> fieldColumnMap = new HashMap<>();
            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                fieldColumnMap.put(dbColumnInfo.getFieldName(), dbColumnInfo.getColumnName());

                Object o = BeanReflectionUtil.getFieldValue(entity, dbColumnInfo.getFieldName());
                if (o != null && !"".equals(o.toString())) {
                    where.append(" and ").append(dbColumnInfo.getColumnName()).append(" =?");
                    propertyValue.add(o);
                }
            }

            String sql = null;
            if (columns == null || columns.length == 0) {
                sql = "select " + AnnotationParser.getTableAllColumn(entity) + "  from  " + tableName;
                columns = AnnotationParser.getTableAllColumn(entity).split(",");
            } else {
                StringBuilder columnsName = new StringBuilder(20);
                for (String str : columns) {
                    columnsName.append(str).append(", ");
                }
                sql = "select " + columnsName.toString().substring(0, columnsName.length() - 2) + "  from  " + tableName;
            }
            //带条件的查询

            if (propertyValue.size() > 0) {
                sql += " where " + where.toString().substring(4);
            }
            SqlRowSet resultSet = this.getJdbcTemplate().queryForRowSet(sql, propertyValue.toArray());


            Set<String> columnSet = new HashSet<>();

            for (String str : columns) {
                columnSet.add(str.trim());
            }

            while (resultSet.next()) {
                @SuppressWarnings("unchecked")
                T temp = (T) BeanReflectionUtil.newInstance(entity.getClass().getName());
                Field[] fields = temp.getClass().getDeclaredFields();
                for (Field field : fields) {
                    //表字段存在才有意义
                    if (columnSet.contains(fieldColumnMap.get(field.getName()))) {
                        field.setAccessible(true);
                        field.set(result, resultSet.getObject(fieldColumnMap.get(field.getName())));
                    }
                }
                result.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public <T> List<T> getEntityListBySQL(String sql, T entity) {
        return getEntityListBySQL(sql, null, entity);
    }

    @Override
    public <T> List<T> getEntityListBySQL(String sql, Object[] args, T entity) {
        List<T> list = new ArrayList<>();
        try {

            if (entity.getClass().getAnnotation(Entity.class) == null) {
                return (List<T>) this.getJdbcTemplate().queryForList(sql, args, entity.getClass());
            }

            SqlRowSet result = this.getJdbcTemplate().queryForRowSet(sql, args);

            SqlRowSetMetaData metaData = result.getMetaData();
            String[] columnNames = metaData.getColumnNames();

            List<DBColumnInfo> dbColumnInfos = AnnotationParser.getAllDBColumnInfo(entity);

            Map<String, String> fieldColumnMap = new HashMap<>();

            for (DBColumnInfo dbColumnInfo : dbColumnInfos) {
                fieldColumnMap.put(dbColumnInfo.getFieldName(), dbColumnInfo.getColumnName());
            }

            Set<String> columnSet = new HashSet<>();

            for (String str : columnNames) {
                columnSet.add(str.trim());
            }

            //obj 获得字段
            Field[] fields = BeanReflectionUtil.getBeanDeclaredFields(entity.getClass().getName());
            while (result.next()) {
                @SuppressWarnings("unchecked")
                T temp = (T) BeanReflectionUtil.newInstance(entity.getClass().getName());
                for (Field field : fields) {
                    //表字段存在才有意义
                    if (columnSet.contains(fieldColumnMap.get(field.getName()))) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        field.set(temp, result.getObject(fieldColumnMap.get(field.getName())));
                    }
                }
                list.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public <T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public <T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy) {
        return null;
    }

    @Override
    public <T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy, String... columns) {
        return null;
    }

    @Override
    public <T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public <T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize, String orderBy) {
        return null;
    }

    @Override
    public <T> PageListContent<T> getEntityPageListBySql(String sql, Object[] args, T entity, int pageNum, int pageSize, String orderBy) {
        return null;
    }

    private JdbcTemplate getJdbcTemplate() {
        return JdbcTemplateHolder.getInstance().getJdbcTemplate();
    }

}
