/**
 * 创建 @ 2013年7月16日 下午3:28:07
 * 
 */
package cong.commons.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *         <p>
 *         数据库操作辅助类，封装了一些常用数据库操作
 *         </p>
 */
public class DBHelper {

    /**
     * 格式化java.util.Date要用的格式，应为作为字符串插入数据库时的格式
     */
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @return the dbDateFormat
     */
    public SimpleDateFormat getDbDateFormat() {
        return this.dbDateFormat;
    }

    /**
     * @param dbDateFormat
     *            the dbDateFormat to set
     */
    public void setDbDateFormat(SimpleDateFormat dbDateFormat) {
        this.dbDateFormat = dbDateFormat;
    }

    /*
     * 内置的存储dbHelper的一个map
     */
    private static Map<Object, DBHelper> dbHelpers = new HashMap<Object, DBHelper>();

    /**
     * 毫无疑问的数据源
     */
    private DataSource                   ds;

    public DBHelper(DataSource ds) {
        this.ds = ds;
        dbHelpers.put(ds, this);
    }

    public DBHelper(DataSource ds, Object key) {
        this(ds);
        dbHelpers.put(key, this);
    }

    /**
     * 从内置的map中取出对应的DBHelper.如果不存在，且key为DataSource的实例，则new一个
     * 
     * @param key
     * @return DBHelper，没有返回null
     */
    public static DBHelper getDBHelper(Object key) {
        DBHelper dbHelper = dbHelpers.get(key);
        if (dbHelper == null && key instanceof DataSource) {
            dbHelper = new DBHelper((DataSource) key);
        }
        return dbHelper;
    }

    /**
     * 保存使用过的对象的相关信息
     */
    private static Map<Class<?>, EntityClassInfo<?>> classInfoCache            = new HashMap<Class<?>, EntityClassInfo<?>>();

    public static final int                          DEFAULT_SQL_BUFFER_LENGTH = 128;

    private static final Logger                      log                       = LoggerFactory.getLogger(DBHelper.class);

    /**
     * 根据主键删除
     * 
     * @param t
     *            带有要删除的主键信息的对象
     * @return Effected Rows
     */
    public <T> int delete(T t) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) t.getClass();
        EntityClassInfo<T> classInfo = getClassInfo(clazz);
        String tableName = classInfo.getDBTableName();
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("delete from ");
        sb.append(tableName);
        sb.append(" where (");
        KeyType keyType = getKeyType(fieldInfoList);
        if (keyType == KeyType.NONE) {
            return -1;
        }
        List<EntityFieldInfo> keyFieldInfoList = classInfo.getKeyFieldInfoList();
        for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
            sb.append(keyFieldInfo.getDBFieldName());
            sb.append(" = ");
            if (!keyFieldInfo.isBooleanType()) {
                sb.append("'");
            }
            sb.append(keyFieldInfo.invokeGetMethod(t));
            if (!keyFieldInfo.isBooleanType()) {
                sb.append("'");
            }
            sb.append(" and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        sb.append(")");
        String deleteSQL = sb.toString();
        int effectedRows = executeUpdateSQL(deleteSQL);
        return effectedRows;
    }

    /**
     * 用给定的参数执行查询PreparedStatement，只查询一个.一般用于用主键查询
     * 
     * @param prepareSQL
     *            要准备的SQL
     * @param params
     *            给定的参数，用来填充？
     * @param clazz
     *            返回值的类型
     * @return 一个查询结果
     */
    public <T> T executeQueryPreparedStatementGetOne(String prepareSQL,
                                                     List<Object> params,
                                                     Class<T> clazz) {
        T t = null;
        List<T> elist = executeQueryPreparedStatement(prepareSQL, params, clazz);
        if (elist != null && elist.size() > 0) {
            t = elist.get(0);
        }
        return t;
    }

    /**
     * 用给定的参数执行查询PreparedStatement
     * 
     * @param prepareSQL
     *            要准备的SQL
     * @param params
     *            给定的参数，用来填充？
     * @param clazz
     *            返回值的类型
     * @return 查询结果List
     */
    public <T> List<T> executeQueryPreparedStatement(String prepareSQL,
                                                     List<Object> params,
                                                     Class<T> clazz) {
        List<T> tlist = null;
        try {
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(prepareSQL);
            if (log.isDebugEnabled()) {
                log.debug("preparing sql : {}", prepareSQL);
            }
            int i = 1;
            for (Object param : params) {
                pstmt.setObject(i, param);
                i++;
            }
            if (log.isDebugEnabled()) {
                log.debug("PreparedStatement : {}", pstmt);
            }
            ResultSet rs = pstmt.executeQuery();
            tlist = getEntityListFromResultSet(clazz, rs);

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            // do nothing
            e.printStackTrace();
        }
        return tlist;
    }

    /**
     * 执行查询SQL，并返回对象列表。
     * 
     * @param ds
     *            数据源
     * @param clazz
     *            要查询的对象class
     * @param querySQL
     *            查询语句
     * @return 对象列表。异常或查不到返回null
     */
    public <T> List<T> executeQuerySQL(Class<T> clazz, String querySQL) {
        List<T> list = null;
        try {
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();

            if (log.isDebugEnabled()) {
                log.debug("executeQuery");
                log.debug("{}", querySQL);
            }

            ResultSet rs = stmt.executeQuery(querySQL);
            list = getEntityListFromResultSet(clazz, rs);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // 异常就拉倒了，返回null，打印异常，继续工作
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 返回查询的第一个结果 @see #executeQuery(DataSource, Class, String)
     * 
     * @param ds
     *            数据源
     * @param clazz
     *            要查询的对象class
     * @param querySQL
     *            查询语句
     * @return 有结果返回结果，否则null
     */
    public <T> T executeQuerySQLGetFirst(Class<T> clazz, String querySQL) {
        T t = null;
        List<T> list = executeQuerySQL(clazz, querySQL);
        if (list != null) {
            t = list.get(0);
        }
        return t;
    }

    /**
     * 用给定的参数执行查询PreparedStatement
     * 
     * @param prepareSQL
     *            要准备的SQL
     * @param params
     *            参数列表
     * @return Effected Rows
     */
    public int executeUpdatePreparedStatement(String prepareSQL, List<Object> params) {
        int effectedRows = -1;
        try {
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(prepareSQL);
            if (log.isDebugEnabled()) {
                log.debug("prepareSQL : {}", prepareSQL);
            }
            int i = 1;
            for (Object param : params) {
                pstmt.setObject(i, param);
                i++;
            }
            if (log.isDebugEnabled()) {
                log.debug("PreparedStatement : {}", pstmt);
            }
            effectedRows = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            // do nothing
            e.printStackTrace();
        }
        return effectedRows;
    }

    /**
     * 执行insert/update/delete语句,返回受影响行数
     * 
     * @param ds
     *            数据源
     * @param updateSQL
     *            SQL语句
     * 
     * @return 受影响行数 异常返回-1
     */
    public int executeUpdateSQL(String updateSQL) {
        int effectedRows = -1;
        try {
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            if (log.isDebugEnabled()) {
                log.debug("execute insert/update/delete");
                log.debug("{}", updateSQL);
            }

            effectedRows = stmt.executeUpdate(updateSQL);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // 异常就拉倒了，返回null，打印异常，继续工作
            e.printStackTrace();
        }
        return effectedRows;
    }

    /**
     * 根据自定义的where语句进行查询
     * 
     * @param ds
     *            数据源
     * @param clazz
     *            类
     * @param whereSQL
     *            where语句(不包括"where")
     * @return 查询结果
     */
    public <T> List<T> executeWhereQuery(Class<T> clazz, String whereSQL) {
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("select * from ");
        EntityClassInfo<T> classInfo = getClassInfo(clazz);
        sb.append(classInfo.getDBTableName());
        sb.append(" where ");
        sb.append(whereSQL);
        return executeQuerySQL(clazz, sb.toString());
    }

    /**
     * 配合classInfoCache的方法，获取ClassInfo。会存储曾经查询获得ClassInfo到一个HashMap中
     * 
     * @param clazz
     *            要查询的类
     * @return EntityClassInfo
     */
    protected <T> EntityClassInfo<T> getClassInfo(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        EntityClassInfo<T> classInfo = (EntityClassInfo<T>) classInfoCache.get(clazz);
        if (classInfo == null) {
            classInfo = new EntityClassInfo<T>(clazz);
            classInfoCache.put(clazz, classInfo);
        }
        return classInfo;
    }

    /**
     * 通过id获取对象，要求有@DBId字段，否则异常
     * 
     * @param ds
     *            数据源
     * @param clazz
     *            要查询的类
     * @param id
     *            ID
     * @return 指定id的对象，查不到返回null
     */
    public <T> T getEntityById(Class<T> clazz, int id) {
        EntityClassInfo<T> classInfo = getClassInfo(clazz);
        String tableName = classInfo.getDBTableName();
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("select * from ");
        sb.append(tableName);
        sb.append(" where ");
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBId()) {
                sb.append(entityFieldInfo.getDBFieldName());
                sb.append(" = ");
                sb.append(id);
                break;
            }
        }
        String querySQL = sb.toString();
        T t = executeQuerySQLGetFirst(clazz, querySQL);

        return t;
    }

    /**
     * 根据对象中包含的主键信息查询出数据库中的对象
     * 
     * @param entity
     *            包含主键信息的对象
     * @return 查询出的数据库中的对象
     */
    public <T> T getEntityByKey(T entity) {
        T t = null;
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) entity.getClass();
        EntityClassInfo<T> classInfo = getClassInfo(clazz);

        String tableName = classInfo.getDBTableName();
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("select * from ");
        sb.append(tableName);
        sb.append(" where ");
        List<EntityFieldInfo> keyFieldInfoList = classInfo.getKeyFieldInfoList();
        for (EntityFieldInfo entityFieldInfo : keyFieldInfoList) {
            sb.append(entityFieldInfo.getDBFieldName());
            sb.append(" = '");
            sb.append(entityFieldInfo.invokeGetMethod(entity));
            sb.append("' and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        String querySQL = sb.toString();

        t = executeQuerySQLGetFirst(clazz, querySQL);

        return t;
    }

    /**
     * 通过name获取对象，要求有@DBName字段，否则异常
     * 
     * @param ds
     *            数据源
     * @param clazz
     *            要查询的类
     * @param name
     *            ID
     * @return 指定name的对象，查不到返回null
     */
    public <T> T getEntityByName(Class<T> clazz, String name) {
        EntityClassInfo<T> classInfo = getClassInfo(clazz);
        String tableName = classInfo.getDBTableName();
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("select * from ");
        sb.append(tableName);
        sb.append(" where ");
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBName()) {
                sb.append(entityFieldInfo.getDBFieldName());
                sb.append(" = '");
                sb.append(name);
                sb.append("'");
                break;
            }
        }
        String querySQL = sb.toString();
        T t = executeQuerySQLGetFirst(clazz, querySQL);

        return t;
    }

    /**
     * 查询有若干个 @DBPK 字段的对象
     * 
     * @param ds
     *            数据源
     * @param entity
     *            "@DBPK" 声明对象的属性值，忽略其他属性
     * @return 查询到的对象，查询不到返回null
     */
    public <T> T getEntityByPK(T entity) {
        T t = null;
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) entity.getClass();
        EntityClassInfo<T> classInfo = getClassInfo(clazz);

        String tableName = classInfo.getDBTableName();
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("select * from ");
        sb.append(tableName);
        sb.append(" where ");
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBPK()) {
                sb.append(entityFieldInfo.getDBFieldName());
                sb.append(" = '");
                sb.append(entityFieldInfo.invokeGetMethod(entity));
                sb.append("' and ");
            }
        }
        sb.delete(sb.length() - 5, sb.length());
        String querySQL = sb.toString();

        t = executeQuerySQLGetFirst(clazz, querySQL);

        return t;
    }

    /**
     * 根据查询结果生成对象类列表
     * 
     * @param clazz
     *            对象类
     * @param rs
     *            查询结果
     * @return 对象列表， 异常或查不到返回null
     */
    public <T> List<T> getEntityListFromResultSet(Class<T> clazz, ResultSet rs) {
        List<T> list = null;
        try {
            list = new ArrayList<T>();
            EntityClassInfo<T> classInfo = new EntityClassInfo<T>(clazz);
            List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();
            while (rs.next()) {
                T entity = clazz.newInstance();
                for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                    if (entityFieldInfo.isDBField()) {
                        try {
                            Object obj = rs.getObject(entityFieldInfo.getDBFieldName());
                            entityFieldInfo.invokeSetMethod(entity, obj);
                        } catch (IllegalArgumentException e) {
                            // 对MySQL boolean做一定处理
                            Class<?> fieldType = entityFieldInfo.getField().getType();
                            if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                                Object obj = rs.getObject(entityFieldInfo.getDBFieldName());
                                if (obj instanceof Integer) {
                                    entityFieldInfo.invokeSetMethod(entity, 0 != (Integer) obj);
                                }
                            }
                        }
                    }
                }
                list.add(entity);
            }
            rs.close();
        } catch (SQLException e) {
            // 什么都不做,只输出异常stack trace
            e.printStackTrace();
        } catch (InstantiationException e) {
            // do nothing
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // do nothing
            e.printStackTrace();
        } catch (SecurityException e) {
            // do nothing
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param fieldInfoList
     * @return
     */
    public static KeyType getKeyType(List<EntityFieldInfo> fieldInfoList) {
        KeyType keyType = KeyType.NONE;

        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBId()) {
                keyType = KeyType.ID;
                break;
            }
        }
        if (keyType == KeyType.NONE) {
            for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                if (entityFieldInfo.isDBName()) {
                    keyType = KeyType.NAME;
                    break;
                }
            }
        }
        if (keyType == KeyType.NONE) {
            for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                if (entityFieldInfo.isDBPK()) {
                    keyType = KeyType.PK;
                    break;
                }
            }
        }
        return keyType;
    }

    public <T> int insert(T t) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) t.getClass();
        EntityClassInfo<T> classInfo = getClassInfo(clazz);
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();

        String tableName = classInfo.getDBTableName();
        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("insert into ");
        sb.append(tableName);
        sb.append(" ( ");

        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBField() && !entityFieldInfo.isDBAI()) {
                sb.append(entityFieldInfo.getDBFieldName());
                sb.append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(") values ( ");
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBField() && !entityFieldInfo.isDBAI()) {
                if (entityFieldInfo.isBooleanType()) {
                    sb.append(entityFieldInfo.invokeGetMethod(t));
                } else if (entityFieldInfo.isDateType()) {
                    sb.append("'");

                    sb.append(dbDateFormat.format(entityFieldInfo.invokeGetMethod(t)));
                    sb.append("'");
                } else {
                    sb.append("'");
                    sb.append(entityFieldInfo.invokeGetMethod(t));
                    sb.append("'");
                }
                sb.append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");

        String insertSQL = sb.toString();
        int effectedRows = executeUpdateSQL(insertSQL);
        return effectedRows;
    }

    public <T> int preparedDeleteByKey(T t) {
        @SuppressWarnings("unchecked")
        EntityClassInfo<T> classInfo = getClassInfo((Class<T>) t.getClass());
        String deleteSQLForPrepare = classInfo.getDeleteSQLForPrepare();
        if (deleteSQLForPrepare == null) {
            return -1;
        }
        List<EntityFieldInfo> keyFieldInfoList = classInfo.getKeyFieldInfoList();
        List<Object> params = new ArrayList<Object>();
        for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
            params.add(keyFieldInfo.invokeGetMethod(t));
        }
        return executeUpdatePreparedStatement(deleteSQLForPrepare, params);
    }

    public <T> int preparedUpdateByKey(T t) {
        @SuppressWarnings("unchecked")
        EntityClassInfo<T> classInfo = getClassInfo((Class<T>) t.getClass());
        String updateSQLForPrepare = classInfo.getUpdateSQLForPrepare();
        if (updateSQLForPrepare == null) {
            return -1;
        }
        List<EntityFieldInfo> keyFieldInfoList = classInfo.getKeyFieldInfoList();
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();

        List<Object> params = new ArrayList<Object>();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBField()
                && !entityFieldInfo.isDBAI()
                && !keyFieldInfoList.contains(entityFieldInfo)) {
                params.add(entityFieldInfo.invokeGetMethod(t));
            }
        }
        for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
            params.add(keyFieldInfo.invokeGetMethod(t));
        }
        return executeUpdatePreparedStatement(updateSQLForPrepare, params);
    }

    public <T> int preperedInsert(T t) {
        @SuppressWarnings("unchecked")
        EntityClassInfo<T> classInfo = getClassInfo((Class<T>) t.getClass());
        String insertSQLForPrepare = classInfo.getInsertSQLForPrepare();
        if (insertSQLForPrepare == null) {
            return -1;
        }
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();
        List<Object> params = new ArrayList<Object>();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBField() && !entityFieldInfo.isDBAI()) {
                params.add(entityFieldInfo.invokeGetMethod(t));
            }
        }
        return executeUpdatePreparedStatement(insertSQLForPrepare, params);
    }

    public <T> T preperedSelectByKey(T t) {
        T result = null;
        @SuppressWarnings("unchecked")
        EntityClassInfo<T> classInfo = getClassInfo((Class<T>) t.getClass());
        String selectSQLForPrepare = classInfo.getSelectSQLForPrepare();
        if (selectSQLForPrepare == null) {
            return null;
        }
        List<EntityFieldInfo> keyFieldInfoList = classInfo.getKeyFieldInfoList();
        List<Object> params = new ArrayList<Object>();
        for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
            params.add(keyFieldInfo.invokeGetMethod(t));
        }
        result = executeQueryPreparedStatementGetOne(selectSQLForPrepare,
                                                     params,
                                                     classInfo.getClazz());
        return result;
    }

    public <T> int update(T t) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) t.getClass();
        EntityClassInfo<T> classInfo = getClassInfo(clazz);
        String tableName = classInfo.getDBTableName();
        List<EntityFieldInfo> fieldInfoList = classInfo.getFieldInfoList();

        StringBuffer sb = new StringBuffer(DEFAULT_SQL_BUFFER_LENGTH);
        sb.append("update ");
        sb.append(tableName);
        sb.append(" set ");

        KeyType keyType = getKeyType(fieldInfoList);

        if (keyType == KeyType.NONE) {
            return -1;
        }

        List<EntityFieldInfo> keyFieldInfoList = classInfo.getKeyFieldInfoList();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            if (entityFieldInfo.isDBField()
                && !entityFieldInfo.isDBAI()
                && !keyFieldInfoList.contains(entityFieldInfo)) {
                sb.append(entityFieldInfo.getDBFieldName());
                sb.append(" = ");
                if (!entityFieldInfo.isBooleanType()) {
                    sb.append("'");
                }
                sb.append(entityFieldInfo.invokeGetMethod(t));
                if (!entityFieldInfo.isBooleanType()) {
                    sb.append("'");
                }
                sb.append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" where (");
        for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
            sb.append(keyFieldInfo.getDBFieldName());
            sb.append(" = ");
            if (!keyFieldInfo.isBooleanType()) {
                sb.append("'");
            }
            sb.append(keyFieldInfo.invokeGetMethod(t));
            if (!keyFieldInfo.isBooleanType()) {
                sb.append("'");
            }
            sb.append(" and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        sb.append(")");
        String updateSQL = sb.toString();
        int effectedRows = executeUpdateSQL(updateSQL);
        return effectedRows;
    }

}
