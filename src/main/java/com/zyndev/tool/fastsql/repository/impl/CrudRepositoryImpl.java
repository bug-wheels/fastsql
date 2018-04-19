package com.zyndev.tool.fastsql.repository.impl;

import com.zyndev.tool.fastsql.core.PageListContent;
import com.zyndev.tool.fastsql.repository.CrudRepository;

import java.util.List;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
public class CrudRepositoryImpl implements CrudRepository {
    @Override
    public <T> boolean existsById(T entity) {
        return false;
    }

    @Override
    public <T> long count(T entity) {
        return 0;
    }

    @Override
    public <T> int save(T entity) {
        return 0;
    }

    @Override
    public <T> int saveAll(Iterable<T> entities) {
        return 0;
    }

    @Override
    public <T> int save(T entity, boolean ignoreNull) {
        return 0;
    }

    @Override
    public <T> int saveAll(Iterable<T> entities, boolean ignoreNull) {
        return 0;
    }

    @Override
    public <T> int deleteById(T entity) {
        return 0;
    }

    @Override
    public <T> int delete(T entity) {
        return 0;
    }

    @Override
    public <T> int deleteAll(Iterable<T> entities) {
        return 0;
    }

    @Override
    public <T> int update(T entity) {
        return 0;
    }

    @Override
    public <T> int update(T entity, boolean ignoreNull) {
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
        return null;
    }

    @Override
    public <T> T findById(T entity, String... columns) {
        return null;
    }

    @Override
    public <T> List<T> getEntityList(T entity) {
        return null;
    }

    @Override
    public <T> List<T> getEntityList(T entity, String... columns) {
        return null;
    }

    @Override
    public <T> List<T> getEntityListBySQL(String sql, T entity) {
        return null;
    }

    @Override
    public <T> List<T> getEntityListBySQL(String sql, Object[] args, T entity) {
        return null;
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
}
