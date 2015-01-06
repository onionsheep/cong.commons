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
        return queryById(this.clazz, id);
    }

    public ArrayList<T> queryByField(String columnName, Object fieldValue) {
        return queryByField(this.clazz, columnName, fieldValue);
    }

    public T queryOneByField(String columnName, Object fieldValue) {
        return queryOneByField(this.clazz, columnName, fieldValue);
    }

    public ArrayList<T> queryByPageMySQL(int pageNum, int pageSize) {
        return queryByPageMySQL(this.clazz, pageNum, pageSize);
    }

    public ArrayList<T> queryByPageMySQL(int pageNum, int pageSize, String whereClause, Object... params) {
        return queryByPageMySQL(this.clazz, pageNum, pageSize, whereClause, params);
    }

    public ArrayList<T> queryBySQLWhereClause(String whereClause, Object... params) {
        return queryBySQLWhereClause(this.clazz, whereClause, params);
    }

    public ArrayList<T> queryEntityListBySQL(String sql, Object... params) {
        return queryEntityListBySQL(this.clazz, sql, params);
    }

    public ArrayList<Object> queryObjectList(String sql, Object... params) {
        return queryObjectList(this.clazz, sql, params);
    }

    public int deleteById(Object id) {
        return deleteById(this.clazz, id);
    }

    public Long count() {
        return count(this.clazz);
    }

    public Long count(String whereClause, Object... params) {
        return count(this.clazz, whereClause, params);
    }
}
