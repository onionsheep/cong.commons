package cong.common.dao;


import cong.common.dao.annotation.Id;
import cong.common.dao.annotation.NotInDB;
import cong.common.dao.annotation.Table;
import cong.common.dao.sqlcache.FieldColumnMapMaker;
import cong.common.dao.sqlcache.LowerCaseTableNameMaker;
import cong.common.dao.sqlcache.OriginalNameFieldColumnMapMaker;
import cong.common.dao.sqlcache.TableNameMaker;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SQLCache {

    public static final String SQL_TYPE_ADD = "ADD";
    public static final String SQL_TYPE_DELETE = "DELETE";
    public static final String SQL_TYPE_UPDATE = "UPDATE";
    public static final String SQL_TYPE_QUERY = "QUERY";

    private static final Logger log = LoggerFactory.getLogger(SQLCache.class);

    public static FieldColumnMapMaker defaultFieldColumnMapMaker = new OriginalNameFieldColumnMapMaker();
    public static TableNameMaker defaultTableNameMaker = new LowerCaseTableNameMaker();


    public static String tableNamePrefix = "t_";
    public static String defaultIdFieldName = "id";

    private static ConcurrentHashMap<Class<?>, SQLCache> sqlCacheMap = new ConcurrentHashMap<Class<?>, SQLCache>();
    private Class<?> clazz;
    private String tableName;
    private Field[] fields;
    private ConcurrentHashMap<String, String> fieldColumnMap;
    private ConcurrentHashMap<String, String> columnFieldMap;
    private ConcurrentHashMap<String, String> sqls;
    private String idColumnName;

    private FieldColumnMapMaker fieldColumnMapMaker = defaultFieldColumnMapMaker;
    private TableNameMaker tableNameMaker = defaultTableNameMaker;


    private SQLCache(Class<?> clazz,
                     String tableName,
                     String idColumnName,
                     Field[] fields,
                     ConcurrentHashMap<String, String> fieldColumnMap,
                     FieldColumnMapMaker fieldColumnMapMaker,
                     TableNameMaker tableNameMaker) {
        if (clazz == null) {
            throw new RuntimeException("making SQLCache error, class param is null!");
        }

        this.clazz = clazz;

        if (StringUtil.isBlank(tableName)) {
            this.tableName = getTableNameFromClass();
        } else {
            this.tableName = tableName;
        }

        if (fields != null) {
            this.fields = fields;
        } else {
            final Field[] declaredFields = this.clazz.getDeclaredFields();
            final ArrayList<Field> fieldArrayList = new ArrayList<Field>();
            for (Field field : declaredFields) {
                final Annotation notInDBAnnotation = field.getAnnotation(NotInDB.class);
                if (notInDBAnnotation != null) {
                    continue;
                }
                fieldArrayList.add(field);
            }
            this.fields = fieldArrayList.toArray(new Field[0]);
        }

        if (fieldColumnMapMaker == null) {
            this.fieldColumnMapMaker = defaultFieldColumnMapMaker;
        } else {
            this.fieldColumnMapMaker = fieldColumnMapMaker;
        }

        if (tableNameMaker == null) {
            this.tableNameMaker = defaultTableNameMaker;
        } else {
            this.tableNameMaker = tableNameMaker;
        }

        this.fieldColumnMap = makeFieldColumnMap(fieldColumnMap);

        this.setFieldColumnMap(this.fieldColumnMap);

        if (StringUtil.isBlank(idColumnName)) {
            this.idColumnName = getIdColumnNameFromClass();
        } else {
            this.idColumnName = idColumnName;
        }

        this.sqls = makeSql();
        sqlCacheMap.put(clazz, this);
        log.debug("new sql cache : {}", this.toString());
    }

    /**
     * 获得一个类对应的SQLCache对象，也可以用来初始化
     *
     * @param clazz               类
     * @param tableName           表名，null时自动调用tableNameMaker生成
     * @param idColumnName        主键列名，null时为 @see SQLCache#defaultIdFieldName
     * @param fields              需要存储到数据库的成员列表，null默认为所有
     * @param fieldColumnNameMap  类的成员名到数据库列名的对应关系，null自动调用fieldColumnMapMaker去生成
     * @param fieldColumnMapMaker fieldColumnNameMap生成器，null时默认为 SQLCache#defaultFieldColumnMapMaker
     * @param tableNameMaker      类名到表名的生成器，默认为 SQLCache#defaultTableNameMaker
     * @return 某个对象对应的用于BaseDao等的SQL相关的缓存。
     */
    public static SQLCache getSqlCache(Class<?> clazz,
                                       String tableName,
                                       String idColumnName,
                                       Field[] fields,
                                       ConcurrentHashMap<String, String> fieldColumnNameMap,
                                       FieldColumnMapMaker fieldColumnMapMaker,
                                       TableNameMaker tableNameMaker) {
        SQLCache sqlCache = sqlCacheMap.get(clazz);
        if (sqlCache == null) {
            synchronized (clazz){
                if(sqlCache == null){
                    sqlCache = new SQLCache(clazz, tableName, idColumnName, fields, fieldColumnNameMap, fieldColumnMapMaker, tableNameMaker);
                    sqlCacheMap.put(clazz, sqlCache);
                }
            }
        }
        return sqlCache;
    }

    public static SQLCache getSqlCache(Class<?> clazz) {
        return getSqlCache(clazz, null, null, null, null, null, null);
    }

    private ConcurrentHashMap<String, String> makeFieldColumnMap(ConcurrentHashMap<String, String> mapToMake) {
        return fieldColumnMapMaker.makeFieldColumnMap(this.getFields(), mapToMake);
    }

    public ConcurrentHashMap<String, String> getColumnFieldMap() {
        if (columnFieldMap == null) {
            fromFieldColumnMapToColumnFieldMap();
        }
        return columnFieldMap;
    }

    public ConcurrentHashMap<String, String> getFieldColumnMap() {
        return fieldColumnMap;
    }

    public void setFieldColumnMap(ConcurrentHashMap<String, String> fieldColumnMap) {
        if (fieldColumnMap != null) {
            this.fieldColumnMap = fieldColumnMap;
            fromFieldColumnMapToColumnFieldMap();
        }
    }

    private void fromFieldColumnMapToColumnFieldMap() {
        this.columnFieldMap = new ConcurrentHashMap<String, String>();
        for (Map.Entry<String, String> e : fieldColumnMap.entrySet()) {
            columnFieldMap.put(e.getValue(), e.getKey());
        }
    }


    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    /**
     * 获得表名，首先检查是否有 @Table 的注解，有则获取其名称，否则调用tableNameMaker生成表名
     *
     * @return 数据库表名
     */
    public String getTableNameFromClass() {
        String tableName = null;
        final Table table = this.clazz.getAnnotation(Table.class);
        if (table != null) {
            tableName = table.name();
        }
        if (StringUtil.isBlank(tableName)) {
            tableName = tableNameMaker.getTableNameFromClass(tableNamePrefix, this.clazz);
        }
        return tableName;
    }

    /**
     * 获得其Id字段对应数据库中的列名。检查字段中是否有 @Id 注解，发现一个则停止继续查询。
     *
     * @return 数据库 主键 字段名
     */
    public String getIdColumnNameFromClass() {
        String idColumnName = null;
        for (Field f : fields) {
            final Id id = f.getAnnotation(Id.class);
            if (id != null) {
                idColumnName = fieldColumnMap.get(f);
                break;
            }
        }
        if (StringUtil.isBlank(idColumnName)) {
            idColumnName = defaultIdFieldName;
        }
        return idColumnName;
    }

    public String getTableName() {
        return tableName;
    }

    public Field[] getFields() {
        return this.fields;
    }

    public ConcurrentHashMap<String, String> getSqls() {
        if (this.sqls == null) {
            this.sqls = makeSql();
        }
        return this.sqls;
    }

    public String getSql(String sqlType) {
        return getSqls().get(sqlType);
    }

    private ConcurrentHashMap<String, String> makeSql() {
        ConcurrentHashMap<String, String> sqlMap = new ConcurrentHashMap<String, String>();

        StringBuilder addSqlStringBuilder = new StringBuilder("insert into ");
        addSqlStringBuilder.append(tableName);
        addSqlStringBuilder.append(" (");

        StringBuilder updateSqlStringBuilder = new StringBuilder("update ");
        updateSqlStringBuilder.append(tableName);
        updateSqlStringBuilder.append(" set ");

        StringBuilder querySqlStringBuilder = new StringBuilder("select ");


        for (Field field : this.fields) {
            String name = field.getName();
            String columnName = fieldColumnMap.get(name);

            if (!idColumnName.equals(name)) {
                updateSqlStringBuilder.append(columnName);
                updateSqlStringBuilder.append("=?,");
            }

            addSqlStringBuilder.append(columnName);
            addSqlStringBuilder.append(",");

            querySqlStringBuilder.append(columnName);
            querySqlStringBuilder.append(",");

        }
        addSqlStringBuilder.deleteCharAt(addSqlStringBuilder.lastIndexOf(","));
        addSqlStringBuilder.append(") values (");

        updateSqlStringBuilder.deleteCharAt(updateSqlStringBuilder.lastIndexOf(","));
        updateSqlStringBuilder.append(" where ");
        updateSqlStringBuilder.append(idColumnName);
        updateSqlStringBuilder.append("=? ");

        querySqlStringBuilder.deleteCharAt(querySqlStringBuilder.lastIndexOf(","));
        querySqlStringBuilder.append(" from ");
        querySqlStringBuilder.append(tableName);
        querySqlStringBuilder.append(" ");

        for (int i = this.fields.length; i > 0; i--) {
            addSqlStringBuilder.append("?,");
        }

        addSqlStringBuilder.deleteCharAt(addSqlStringBuilder.lastIndexOf(","));
        addSqlStringBuilder.append(") ");

        sqlMap.put(SQL_TYPE_ADD, addSqlStringBuilder.toString());
        sqlMap.put(SQL_TYPE_UPDATE, updateSqlStringBuilder.toString());
        sqlMap.put(SQL_TYPE_QUERY, querySqlStringBuilder.toString());
        sqlMap.put(SQL_TYPE_DELETE, "delete from " + tableName + " where " + idColumnName + " = ? ");

        return sqlMap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SQLCache:{");
        sb.append("clazz:").append(clazz);
        sb.append(", tableName:'").append(tableName).append('\'');
        sb.append(", fields:").append(Arrays.toString(fields));
        sb.append(", fieldColumnMap:").append(fieldColumnMap);
        sb.append(", columnFieldMap:").append(columnFieldMap);
        sb.append(", sqls:").append(sqls);
        sb.append(", idColumnName:'").append(idColumnName).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
