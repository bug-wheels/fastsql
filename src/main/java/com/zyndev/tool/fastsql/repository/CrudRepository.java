package com.zyndev.tool.fastsql.repository;

import com.zyndev.tool.fastsql.core.PageListContent;

import java.util.List;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.5
 */
public interface CrudRepository {

    <T> boolean existsById(T entity);

    <T> long count(T entity);

    <T> int save(T entity);

    <T> int saveAll(Iterable<T> entities);

    <T> int save(T entity, boolean ignoreNull);

    <T> int saveAll(Iterable<T> entities, boolean ignoreNull);

    <T> int deleteById(T entity);

    <T> int delete(T entity);

    <T> int deleteAll(Iterable<T> entities);

    <T> int update(T entity);

    <T> int update(T entity, boolean ignoreNull);

    <T> int update(Iterable<T> entities);

    <T> int update(Iterable<T> entities, boolean ignoreNull);

    <T> T findById(T entity);

    <T> T findById(T entity, String... columns);

    <T> List<T> getEntityList(T entity);

    <T> List<T> getEntityList(T entity, String... columns);

    <T> List<T> getEntityListBySQL(String sql, T entity);

    <T> List<T> getEntityListBySQL(String sql, Object[] args, T entity);

    <T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize);

    <T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy);

    <T> PageListContent<T> getEntityPageList(T entity, int pageNum, int pageSize, String orderBy, String... columns);

    <T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize);

    <T> PageListContent<T> getEntityPageListBySql(String sql, T entity, int pageNum, int pageSize, String orderBy);

    <T> PageListContent<T> getEntityPageListBySql(String sql, Object[] args, T entity, int pageNum, int pageSize, String orderBy);
}
