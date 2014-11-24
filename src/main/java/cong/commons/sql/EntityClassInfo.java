/**
 * 创建 @ 2013年7月23日 下午8:35:21
 * 
 */
package cong.commons.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存类相关信息，数据库表名等
 * 
 * @param <T>
 */
public class EntityClassInfo<T> {
    private Class<T>        clazz               = null;
    private String          dBTableName         = null;
    private String          deleteSQLForPrepare = null;

    private List<EntityFieldInfo> fieldInfoList       = null;
    private String          insertSQLForPrepare = null;

    private EntityFieldInfo       keyFieldInfo        = null;
    private List<EntityFieldInfo> keyFieldInfoList    = null;
    private String          selectSQLForPrepare = null;
    private String          updateSQLForPrepare = null;

    public EntityClassInfo(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    /**
     * 
     * @return @DBTable 指定的表名，否则为类名
     */
    public String getDBTableName() {
        if (dBTableName == null) {
            DBTable dBTable = clazz.getAnnotation(DBTable.class);
            if (dBTable == null) {
                dBTableName = clazz.getSimpleName();
            } else {
                dBTableName = dBTable.value();
            }
        }
        return dBTableName;
    }

    /**
     * 
     * @return 以问号作为占位符的，以主键作为查询条件的SQL删除语句
     */
    public String getDeleteSQLForPrepare() {
        if (deleteSQLForPrepare == null) {
            KeyType keyType = DBHelper.getKeyType(this.getFieldInfoList());
            if (keyType == KeyType.NONE) {
                return null;
            }
            StringBuffer sb = new StringBuffer(DBHelper.DEFAULT_SQL_BUFFER_LENGTH);
            sb.append("delete from ");
            sb.append(this.getDBTableName());
            sb.append(" where (");
            this.keyFieldInfoList = this.getKeyFieldInfoList();
            for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
                sb.append(keyFieldInfo.getDBFieldName());
                sb.append(" = ? and ");
            }
            sb.delete(sb.length() - 5, sb.length());
            sb.append(")");
            deleteSQLForPrepare = sb.toString();
        }
        return this.deleteSQLForPrepare;
    }

    /**
     * 
     * @return 字段信息List
     */
    public List<EntityFieldInfo> getFieldInfoList() {
        if (fieldInfoList == null) {
            fieldInfoList = new ArrayList<EntityFieldInfo>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                fieldInfoList.add(new EntityFieldInfo(field));
            }
        }
        return fieldInfoList;
    }

    /**
     * 
     * @return 以问号作为占位符的SQL Insert语句
     */
    public String getInsertSQLForPrepare() {
        if (this.insertSQLForPrepare == null) {
            StringBuffer sb = new StringBuffer(DBHelper.DEFAULT_SQL_BUFFER_LENGTH);
            sb.append("insert into ");
            sb.append(this.getDBTableName());
            sb.append(" (");
            this.fieldInfoList = this.getFieldInfoList();
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
                    sb.append(" ?, ");
                }
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(")");
            insertSQLForPrepare = sb.toString();

        }
        return this.insertSQLForPrepare;
    }

    public EntityFieldInfo getKeyFieldInfo() {
        if (keyFieldInfo == null) {
            KeyType keyType = DBHelper.getKeyType(fieldInfoList);
            if (keyType == KeyType.PK || keyType == KeyType.NONE) {
                return null;
            }
            if (keyType == KeyType.ID) {
                for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                    if (entityFieldInfo.isDBId()) {
                        keyFieldInfo = entityFieldInfo;
                        break;
                    }
                }
            } else if (keyType == KeyType.NAME) {
                for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                    if (entityFieldInfo.isDBName()) {
                        keyFieldInfo = entityFieldInfo;
                        break;
                    }
                }
            }
        }
        return keyFieldInfo;
    }

    public List<EntityFieldInfo> getKeyFieldInfoList() {
        if (keyFieldInfoList == null) {
            KeyType keyType = DBHelper.getKeyType(this.getFieldInfoList());

            keyFieldInfoList = new ArrayList<EntityFieldInfo>();
            switch (keyType) {
            case ID:
                for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                    if (entityFieldInfo.isDBId()) {
                        keyFieldInfoList.add(entityFieldInfo);
                        break;
                    }
                }
                break;
            case NAME:
                for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                    if (entityFieldInfo.isDBName()) {
                        keyFieldInfoList.add(entityFieldInfo);
                        break;
                    }
                }
                break;
            case PK:
                for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                    if (entityFieldInfo.isDBPK()) {
                        keyFieldInfoList.add(entityFieldInfo);
                    }
                }
                break;
            case NONE:
                return null;
            }
        }
        return keyFieldInfoList;
    }

    /**
     * 
     * @return 以问号作为占位符的，以主键为查询条件的SQL Select语句
     */
    public String getSelectSQLForPrepare() {
        if (this.selectSQLForPrepare == null) {
            StringBuffer sb = new StringBuffer(DBHelper.DEFAULT_SQL_BUFFER_LENGTH);
            sb.append("select * from ");
            sb.append(this.getDBTableName());
            sb.append(" where ");

            this.keyFieldInfoList = this.getKeyFieldInfoList();
            for (EntityFieldInfo entityFieldInfo : keyFieldInfoList) {
                sb.append(entityFieldInfo.getDBFieldName());
                sb.append(" = ? and ");
            }
            sb.delete(sb.length() - 5, sb.length());
            selectSQLForPrepare = sb.toString();
        }
        return this.selectSQLForPrepare;
    }

    /**
     * 
     * @return 以问号作为占位符的，以主键为查询条件的SQL Update语句
     */
    public String getUpdateSQLForPrepare() {
        if (this.updateSQLForPrepare == null) {
            KeyType keyType = DBHelper.getKeyType(this.getFieldInfoList());
            if (keyType == KeyType.NONE) {
                return null;
            }
            StringBuffer sb = new StringBuffer(DBHelper.DEFAULT_SQL_BUFFER_LENGTH);
            sb.append("update ");
            sb.append(this.getDBTableName());
            sb.append(" set ");

            this.keyFieldInfoList = this.getKeyFieldInfoList();
            for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
                if (entityFieldInfo.isDBField()
                    && !entityFieldInfo.isDBAI()
                    && !keyFieldInfoList.contains(entityFieldInfo)) {
                    sb.append(entityFieldInfo.getDBFieldName());
                    sb.append(" = ?, ");
                }
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(" where (");
            for (EntityFieldInfo keyFieldInfo : keyFieldInfoList) {
                sb.append(keyFieldInfo.getDBFieldName());
                sb.append(" = ? and ");
            }
            sb.delete(sb.length() - 5, sb.length());
            sb.append(")");
            this.updateSQLForPrepare = sb.toString();
        }
        return this.updateSQLForPrepare;
    }
}