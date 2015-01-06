package cong.common.dao;

import java.util.ArrayList;

/**
 * Created by cong on 2015/1/6.
 */
public class TypedDao <T> extends BaseDao {
    private Class<T> clazz;

    public TypedDao() {
        this.clazz = (Class<T>)getClass();
    }

    public ArrayList<T> queryAll() {
        return queryAll(this.clazz);
    }

    public T queryById(Object id) {
        return super.queryById(this.clazz, id);
    }

    public ArrayList<T> queryByField(String columnName, Object fieldValue) {
        return super.queryByField(this.clazz, columnName, fieldValue);
    }

    public T queryOneByField(String columnName, Object fieldValue) {
        return super.queryOneByField(this.clazz, columnName, fieldValue);
    }

    public ArrayList<T> queryByPageMySQL(int pageNum, int pageSize) {
        return super.queryByPageMySQL(this.clazz, pageNum, pageSize);
    }

    public ArrayList<T> queryByPageMySQL(int pageNum, int pageSize, String whereClause, Object... params) {
        return super.queryByPageMySQL(this.clazz, pageNum, pageSize, whereClause, params);
    }

    public ArrayList<T> queryBySQLWhereClause(String whereClause, Object... params) {
        return super.queryBySQLWhereClause(this.clazz, whereClause, params);
    }

    public ArrayList<T> queryEntityListBySQL(String sql, Object... params) {
        return super.queryEntityListBySQL(this.clazz, sql, params);
    }

    public ArrayList<Object> queryObjectList(String sql, Object... params) {
        return super.queryObjectList(this.clazz, sql, params);
    }

    public int deleteById(Object id) {
        return super.deleteById(this.clazz, id);
    }

    public Long count() {
        return super.count(this.clazz);
    }

    public Long count(String whereClause, Object... params) {
        return super.count(this.clazz, whereClause, params);
    }
}
