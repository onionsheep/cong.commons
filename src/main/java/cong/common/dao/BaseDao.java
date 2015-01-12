/**
 * 2014年2月21日 下午4:43:17
 */
package cong.common.dao;

import cong.common.util.DBUtil;
import cong.common.util.Pair;
import jodd.bean.BeanUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author cong 刘聪 onion_sheep@163.com|onionsheep@gmail.com
 *         首先，不处理任何特殊情况；所有名称驼峰转下划线；表名为#SQLCache.tableNamePrefix_加类名；每个表必须有个字段叫id，而且是整数自增主键；
 */
public class BaseDao {
    private static final Logger log = LoggerFactory.getLogger(BaseDao.class);

    /**
     * 获得mysql分页语句，如果传入的参数不合法，默认第一页，每页10条
     *
     * @param pageNum     页码
     * @param pageSize 页大小
     * @return mysql的分页语句，类似于limit 90, 100 这种，打头是一个空格，便于处理
     */
    public static String getMySQLPageLimit(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        int first = (pageNum - 1) * pageSize;
        final StringBuilder sb = new StringBuilder(20);
        sb.append(" limit ");
        sb.append(first);
        sb.append(", ");
        sb.append(pageSize);
        sb.append(" ");
        return sb.toString();
    }

    /**
     * 向数据库中添加一个对象，使用默认连接，默认数据库。表名字段名使用SQLCache中定义的规则
     *
     * @param t   对象
     * @param <T> 对象的类型
     * @return 数据库执行完添加语句的返回值，影响的行数
     */
    public <T> int add(T t) {
        final Pair<String, Object[]> pair = prepareAdd(t);
        return executeUpdate(pair.getV1(), pair.getV2());
    }

    public <T> Pair<Integer, Long> addAndGetKey(T t) {
        final Pair<String, Object[]> pair = prepareAdd(t);
        return executeInsert(pair.getV1(), pair.getV2());
    }

    protected <T> Pair<String, Object[]> prepareAdd(T t){
        Class<?> clazz = t.getClass();
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String sql = sqlCache.getSql(SQLCache.SQL_TYPE_ADD);
        Field[] fields = sqlCache.getFields();
        Object[] params = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Object value = BeanUtil.getDeclaredProperty(t, fields[i].getName());
            params[i] = value;
        }
        return new Pair<>(sql, params);
    }

    /**
     * 根据ID更新一个数据库中的对象
     *
     * @param t   要更新的对象
     * @param <T> 对象的类型
     * @return 数据库更新语句默认，影响的行数
     */
    public <T> int update(T t) {
        Class<?> clazz = t.getClass();
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String sql = sqlCache.getSql(SQLCache.SQL_TYPE_UPDATE);
        Field[] fields = sqlCache.getFields();
        String idColumnName = sqlCache.getIdColumnName();
        if (log.isDebugEnabled()) {
            log.debug("id field: {}", idColumnName);
            //log.debug("update sql : {}", sql);
        }
        Object[] params = new Object[fields.length];
        int i = 0;
        for (Field field : fields) {
            if (idColumnName.equals(field.getName())) {
                continue;
            }
            Object value = BeanUtil.getDeclaredProperty(t, field.getName());
            params[i] = value;
            i++;
        }
        params[i] = BeanUtil.getDeclaredProperty(t, idColumnName);

        return executeUpdate(sql, params);
    }

    /**
     * 根据ID更新一个数据库中的对象, 忽略对象中为null的字段
     *
     * @param t   要更新的对象
     * @param <T> 对象的类型
     * @return 数据库更新语句默认，影响的行数
     */
    public <T> int updateIgnoreNull(T t) {
        Class<?> clazz = t.getClass();
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);

        ArrayList<Field> notNullFieldList = new ArrayList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            Object property = BeanUtil.getDeclaredPropertySilently(t, f.getName());
            if (property != null) {
                notNullFieldList.add(f);
            }
        }

        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append(" update ");
        sqlsb.append(sqlCache.getTableName());
        sqlsb.append(" set ");
        sqlCache.getFieldColumnMap();
        String idColumnName = sqlCache.getIdColumnName();
        for (Field f : notNullFieldList) {
            String name = f.getName();
            if (idColumnName.equals(name)) {
                continue;
            }
            sqlsb.append(name);
            sqlsb.append(" = ?, ");
        }
        sqlsb.deleteCharAt(sqlsb.lastIndexOf(","));
        sqlsb.append(" where ");
        sqlsb.append(idColumnName);
        sqlsb.append("=? ");
        String sql = sqlsb.toString();

        Object[] params = new Object[notNullFieldList.size()];
        int i = 0;
        for (Field field : notNullFieldList) {
            if (idColumnName.equals(field.getName())) {
                continue;
            }
            Object value = BeanUtil.getDeclaredProperty(t, field.getName());
            params[i] = value;
            i++;
        }
        params[i] = BeanUtil.getDeclaredProperty(t, idColumnName);

        return executeUpdate(sql, params);
    }

    /**
     * 查询某个类对应表的所有数据
     *
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象的ArrayList
     */
    public <T> ArrayList<T> queryAll(Class<T> clazz) {
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String sql = sqlCache.getSql(SQLCache.SQL_TYPE_QUERY);
        return queryEntityListBySQL(clazz, sql);
    }

    /**
     * 根据id查询对象
     *
     * @param clazz 对象类型
     * @param id    ID
     * @param <T>   对象类型
     * @return 有一个或多个返回第一个，没有返回null
     */
    public <T> T queryById(Class<T> clazz, Object id) {
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String tableName = sqlCache.getTableName();
        String idColumnName = sqlCache.getIdColumnName();
        String sql = "select * from " + tableName + " where " + idColumnName + " = ?";
        ArrayList<T> list = queryEntityListBySQL(clazz, sql, id);
        if ((list != null) && (list.size() > 0)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据字段相等查询
     *
     * @param clazz      对象类型
     * @param columnName 字段名，数据库中的名字
     * @param fieldValue 字段值
     * @param <T>        对象类型
     * @return 对象列表
     */
    public <T> ArrayList<T> queryByField(Class<T> clazz, String columnName, Object fieldValue) {
        String tableName = SQLCache.getSqlCache(clazz).getTableName();
        String sql = "select * from " + tableName + " where " + columnName + " = ?";
        ArrayList<T> list = queryEntityListBySQL(clazz, sql, fieldValue);
        return list;
    }

    /**
     * 根据字段相等查询一个
     *
     * @param clazz      对象类型
     * @param columnName 字段名，数据库中的名字
     * @param fieldValue 字段值
     * @param <T>        对象类型
     * @return 一个对象，没有则为null
     */
    public <T> T queryOneByField(Class<T> clazz, String columnName, Object fieldValue) {
        ArrayList<T> list = queryByField(clazz, columnName, fieldValue);
        if ((list != null) && (list.size() > 0)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 无条件mysql分页查询
     *
     * @param clazz    对象类型
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param <T>      对象类型
     * @return 对象列表
     */
    public <T> ArrayList<T> queryByPageMySQL(Class<T> clazz, int pageNum, int pageSize) {
        return queryByPageMySQL(clazz, pageNum, pageSize, null);
    }

    /**
     * mysql分页查询，带有条件
     *
     * @param clazz       对象类型
     * @param pageNum     页码
     * @param pageSize    页大小
     * @param whereClause where语句，用?做参数
     * @param params      参数列表，顺序where语句中的顺序一致
     * @param <T>         对象类型
     * @return 对象列表，空的话返回空列表，非null
     */
    public <T> ArrayList<T> queryByPageMySQL(Class<T> clazz, int pageNum, int pageSize, String whereClause, Object... params) {
        ArrayList<T> list = new ArrayList<T>();
        if ((pageNum < 1) || (pageSize < 1)) {
            return list;
        }
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String sql = sqlCache.getSql(SQLCache.SQL_TYPE_QUERY);
        if (!StringUtil.isBlank(whereClause)) {
            sql += (" " + whereClause);
        }
        sql += getMySQLPageLimit(pageNum, pageSize);
        list = queryEntityListBySQL(clazz, sql, params);
        return list;
    }

    /**
     * 带有where从句的sql查询
     *
     * @param clazz       对象类型
     * @param whereClause where从句
     * @param params      参数列表
     * @param <T>         对象类型
     * @return 对象列表，空的话返回空列表，非null
     */
    public <T> ArrayList<T> queryBySQLWhereClause(Class<T> clazz, String whereClause, Object... params) {
        ArrayList<T> list;
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String sql = sqlCache.getSql(SQLCache.SQL_TYPE_QUERY) + whereClause;
        list = queryEntityListBySQL(clazz, sql, params);
        return list;
    }


    /**
     * 带有where从句的sql查询单个对象
     *
     * @param clazz       对象类型
     * @param whereClause where从句
     * @param params      参数列表
     * @param <T>         对象类型
     * @return 对象，查询不到返回null
     */
    public <T> T queryOneBySQLWhereClause(Class<T> clazz, String whereClause, Object... params) {
        T t = null;
        final ArrayList<T> list = queryBySQLWhereClause(clazz, whereClause, params);
        if(list != null && list.size() > 0){
            t = list.get(0);
        }
        return t;
    }

    /**
     * 使用sql语句查询
     *
     * @param clazz  对象类型
     * @param sql    sql语句，参数?表示
     * @param params 参数列表
     * @param <T>    对象类型
     * @return 对象列表，空的话返回空列表，非null
     */
    public <T> ArrayList<T> queryEntityListBySQL(Class<T> clazz, String sql, Object... params) {
        final Pair<Connection, ResultSet> connectionResultSetPair = executeQuery(sql, params);
        ArrayList<T> queryList = DBUtil.getBeanListFromResultSet(connectionResultSetPair.getV2(), clazz);
        closeResultAndConnection(connectionResultSetPair.getV2(), connectionResultSetPair.getV1());

        if (queryList == null) {
            queryList = new ArrayList<T>();
        }
        return queryList;
    }

    /**
     * 使用sql语句查询，只返回一个对象
     *
     * @param clazz  对象类型
     * @param sql    sql语句，参数?表示
     * @param params 参数列表
     * @param <T>    对象类型
     * @return 单个查询的对象，如果查不到，则返回null
     */
    public <T> T queryOneEntityBySQL(Class<T> clazz, String sql, Object... params) {
        T t = null;
        final Pair<Connection, ResultSet> connectionResultSetPair = executeQuery(sql, params);
        ArrayList<T> queryList = DBUtil.getBeanListFromResultSet(connectionResultSetPair.getV2(), clazz);
        closeResultAndConnection(connectionResultSetPair.getV2(), connectionResultSetPair.getV1());

        if (queryList != null && queryList.size() > 0) {
            t = queryList.get(0);
        }
        return t;
    }


    /**
     * 使用SQL语句查询
     *
     * @param sql    SQL语句
     * @param params 参数列表
     * @return 对象列表，MapList
     */
    public ArrayList<HashMap<String, Object>> queryMapListBySQL(String sql, Object... params) {
        final Pair<Connection, ResultSet> connectionResultSetPair = executeQuery(sql, params);
        ArrayList<HashMap<String, Object>> mapList = DBUtil.getMapListFromResultSet(connectionResultSetPair.getV2());
        closeResultAndConnection(connectionResultSetPair.getV2(), connectionResultSetPair.getV1());
        if (mapList == null) {
            mapList = new ArrayList<HashMap<String, Object>>();
        }
        return mapList;
    }

    /**
     * 使用SQL语句查询单个对象
     *
     * @param sql    SQL语句
     * @param params 参数列表
     * @return 单个对象 Map
     */
    public HashMap<String, Object> queryOneMapBySQL(String sql, Object... params) {
        final ArrayList<HashMap<String, Object>> maps = queryMapListBySQL(sql, params);
        if (maps.size() > 0) {
            return maps.get(0);
        } else {
            return new HashMap<String, Object>();
        }
    }

    /**
     * 使用SQL语句查询
     *
     * @param sql    SQL语句
     * @param params 参数列表
     * @return Object数组列表
     */
    public ArrayList<Object[]> queryObjectArrayList(String sql, Object... params) {
        final Pair<Connection, ResultSet> connectionResultSetPair = executeQuery(sql, params);
        ArrayList<Object[]> list = DBUtil.getObjectArrayListFromResultSet(connectionResultSetPair.getV2());
        closeResultAndConnection(connectionResultSetPair.getV2(), connectionResultSetPair.getV1());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 使用SQL语句查询
     *
     * @param sql    SQL语句
     * @param params 参数列表
     * @return Object列表
     */
    public ArrayList<Object> queryObjectList(String sql, Object... params) {
        return queryObjectList(Object.class, sql, params);
    }

    /**
     * 使用SQL语句查询
     *
     * @param clazz  对象类型
     * @param sql    SQL语句
     * @param params 参数列表
     * @param <T>    对象类型
     * @return 指定类型的对象的列表
     */
    public <T> ArrayList<Object> queryObjectList(Class<T> clazz, String sql, Object... params) {
        ArrayList<Object> list = new ArrayList<>();
        final ArrayList<Object[]> objectsList = queryObjectArrayList(sql, params);
        for (int len = objectsList.size(), i = 0; i < len; i++) {
            final Object[] objects = objectsList.get(i);
            if (objects != null && objects.length > 0) {
                Object obj = objects[0];
                if (obj.getClass().equals(clazz)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 执行sql查询，数据库连接使用 DBUtil.getConnection()，并返回，在需要的时候关闭
     *
     * @param sql    sql语句，参数用?表示
     * @param params 参数列表
     * @return Pair&lt;数据库连接，结果集&gt;
     */
    public Pair<Connection, ResultSet> executeQuery(String sql, Object... params) {
        if (log.isDebugEnabled()) {
            logSQLAndParams(sql, params);
        }
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            setParamsIntoPrepareStatement(prepareStatement, params);
            resultSet = prepareStatement.executeQuery();
        } catch (SQLException e) {
            log.error("SQL异常,{}", e.getMessage());
            e.printStackTrace();
        }
        return new Pair<Connection, ResultSet>(connection, resultSet);
    }

    /**
     * 把SQL和对应的参数输出格式化的日志
     *
     * @param sql    SQL语句
     * @param params 参数列表
     */
    protected void logSQLAndParams(String sql, Object[] params) {
        final StringBuilder sb = new StringBuilder("\n");
        sb.append(sql);
        if (params != null && params.length > 0) {
            sb.append("\n| ");
        }
        for (Object paramObj : params) {
            if (paramObj instanceof String && ((String) paramObj).length() > 100) {
                sb.append("\n");
                sb.append(paramObj);
                sb.append("\n");
            } else {
                sb.append(paramObj);
                sb.append(" | ");
            }
        }
        log.debug("SQL and Param : {}", sb);
    }

    /**
     * 把参数按照顺序设置到准备的sql语句中
     *
     * @param prepareStatement 准备的sql语句
     * @param params           参数列表
     * @throws SQLException 参数列表数量错误，数据库访问错误，数据库连接等已关闭，或者the type of the given object is ambiguous
     */
    public void setParamsIntoPrepareStatement(final PreparedStatement prepareStatement,
                                              final Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            prepareStatement.setObject(i + 1, params[i]);
        }
    }

    /**
     * 关闭结果集和数据库连接
     *
     * @param resultSet  结果集
     * @param connection 数据库连接
     */
    public void closeResultAndConnection(ResultSet resultSet, Connection connection) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            log.error("关闭ResultSet时,SQL异常,{}", e.getMessage());
            e.printStackTrace();
        }

        try {
            if(connection != null && connection.getAutoCommit() && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            log.error("关闭Connection时,SQL异常,{}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 执行update sql语句
     *
     * @param sql    sql语句，参数?表示
     * @param params 参数列表
     * @return 受影响的行数，或者0代表没有任何返回结果，-1代表异常，返回结果参考PreparedStatement.executeUpdate
     * @see PreparedStatement#executeUpdate
     */
    public int executeUpdate(String sql, Object... params) {
        if (log.isDebugEnabled()) {
            logSQLAndParams(sql, params);
        }
        int result = -1;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            if (params != null) {
                setParamsIntoPrepareStatement(prepareStatement, params);
            }
            result = prepareStatement.executeUpdate();
            prepareStatement.close();
            if(connection.getAutoCommit()){
                connection.close();
            }
        } catch (SQLException e) {
            log.error("发生异常 {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 执行一条Insert语句，并返回影响的行数和生成的KEY
     * @param sql 查询语句
     * @param params 参数列表
     * @return Pair(影响行数,生成的KEY)
     */
    public Pair<Integer, Long> executeInsert(String sql, Object... params) {
        if (log.isDebugEnabled()) {
            logSQLAndParams(sql, params);
        }
        Integer result = -1;
        Long key = -1l;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                setParamsIntoPrepareStatement(prepareStatement, params);
            }
            result = prepareStatement.executeUpdate();
            final ResultSet rs = prepareStatement.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = rs.getLong(1);
            }
            prepareStatement.close();
            if(connection.getAutoCommit()){
                connection.close();
            }
        } catch (SQLException e) {
            log.error("发生异常 {}", e.getMessage());
            e.printStackTrace();
        }
        return new Pair<>(result, key);
    }


    /**
     * 从数据库中删除某个对象，根据对象中的id属性作为id字段删除
     *
     * @param t   对象
     * @param <T> 对象类型
     * @return 受影响的行数，或者0代表没有任何返回结果，-1代表异常，返回结果参考PreparedStatement.executeUpdate
     * @see PreparedStatement#executeUpdate
     */
    public <T> int delete(T t) {
        Class<?> clazz = t.getClass();
        return deleteById(clazz, BeanUtil.getDeclaredProperty(t, SQLCache.getSqlCache(clazz).getIdColumnName()));
    }

    /**
     * 从数据库中删除某个对象，根据id字段删除
     *
     * @param clazz 对象类型
     * @param id    id
     * @param <T>   对象类型
     * @return 受影响的行数，或者0代表没有任何返回结果，-1代表异常，返回结果参考PreparedStatement.executeUpdate
     * @see PreparedStatement#executeUpdate
     */
    public <T> int deleteById(Class<T> clazz, Object id) {
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String sql = sqlCache.getSql(SQLCache.SQL_TYPE_DELETE);
        log.debug("delete sql : {}", sql);
        return executeUpdate(sql, id);
    }

    /**
     * 根据id字段统计某种类型对象在数据库的数量
     *
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 符合条件的对象的数量，异常返回-1
     */
    public <T> Long count(Class<T> clazz) {
        return count(clazz, "");
    }


    /**
     * 根据id字段统计某种类型对象在数据库的数量
     *
     * @param clazz       对象类型
     * @param whereClause 统计的where从句，参数用?表示
     * @param params      参数列表
     * @param <T>         对象类型
     * @return 符合条件的对象的数量，异常返回-1
     */
    public <T> Long count(Class<T> clazz, String whereClause, Object... params) {
        Long count = -1l;
        SQLCache sqlCache = SQLCache.getSqlCache(clazz);
        String idColumnName = sqlCache.getIdColumnName();
        String tableName = sqlCache.getTableName();
        String sql = "select count(" + idColumnName + ") from " + tableName + " " + whereClause;
        final Pair<Connection, ResultSet> connectionResultSetPair = executeQuery(sql, params);
        final ResultSet resultSet = connectionResultSetPair.getV2();
        try {
            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error("发生异常 {}", e.getMessage());
            e.printStackTrace();
        }
        closeResultAndConnection(connectionResultSetPair.getV2(), connectionResultSetPair.getV1());
        return count;
    }

}
