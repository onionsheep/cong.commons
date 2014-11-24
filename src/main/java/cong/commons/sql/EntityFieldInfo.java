/**
 * 创建 @ 2013年7月23日 下午8:35:46
 * 
 */
package cong.commons.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cong.commons.lang.StringHelper;

/**
 * 把字段信息包装起来,方便获取
 */
public class EntityFieldInfo {

    private enum BOOL {
        FALSE, TRUE, UNINITLIZED;
    }

    private EntityFieldInfo.BOOL booleanType = BOOL.UNINITLIZED;
    private EntityFieldInfo.BOOL dBAI        = BOOL.UNINITLIZED;
    private EntityFieldInfo.BOOL dBField     = BOOL.UNINITLIZED;
    private String         dBFieldName = null;
    private EntityFieldInfo.BOOL dBId        = BOOL.UNINITLIZED;
    private EntityFieldInfo.BOOL dBName      = BOOL.UNINITLIZED;
    private EntityFieldInfo.BOOL dBPK        = BOOL.UNINITLIZED;
    private Field          field       = null;
    private Method         getMethod   = null;
    private String         name        = null;

    private Method         setMethod   = null;
    private EntityFieldInfo.BOOL DateType    = BOOL.UNINITLIZED;

    public EntityFieldInfo(Field field) {
        this.field = field;
    }

    /**
     * 
     * @return @DBFieldName 指定的数据库字段名，否则为java成员名
     */
    public String getDBFieldName() {
        if (this.dBFieldName == null) {
            DBFieldName dbFieldNameAnnotation = field.getAnnotation(DBFieldName.class);
            if (dbFieldNameAnnotation == null
                || StringHelper.isBlank(dbFieldNameAnnotation.value())) {
                dBFieldName = field.getName();
            } else {
                dBFieldName = dbFieldNameAnnotation.value();
            }
        }
        return this.dBFieldName;
    }

    /**
     * @return java Field
     */
    public Field getField() {
        return this.field;
    }

    /**
     * 
     * @return get方法,没有或者访问不到，则返回null
     */
    public Method getGetMethod() {
        if (this.getMethod == null) {
            try {
                String prefix = "get";
                if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                    prefix = "is";
                }
                this.getMethod = field.getDeclaringClass()
                                      .getMethod(prefix
                                                 + StringHelper.firstToUpperCase(field.getName()));
            } catch (NoSuchMethodException e) {
                // do nothing
                e.printStackTrace();
            } catch (SecurityException e) {
                // do nothing
                e.printStackTrace();
            }
        }
        return this.getMethod;
    }

    /**
     * 
     * @return java 成员名
     */
    public String getName() {
        if (this.name == null) {
            this.name = field.getName();
        }
        return this.name;
    }

    /**
     * 
     * @return set方法,没有或者访问不到，则返回null
     */
    public Method getSetMethod() {
        if (this.setMethod == null) {
            try {
                this.setMethod = field.getDeclaringClass()
                                      .getMethod("set"
                                                         + StringHelper.firstToUpperCase(field.getName()),
                                                 field.getType());
            } catch (NoSuchMethodException e) {
                // do nothing
                e.printStackTrace();
            } catch (SecurityException e) {
                // do nothing
                e.printStackTrace();
            }
        }
        return this.setMethod;
    }

    public Object invokeGetMethod(Object entityObj) {
        Object fieldObj = null;
        try {
            fieldObj = this.getGetMethod().invoke(entityObj);
        } catch (IllegalAccessException e) {
            // do nothing
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // do nothing
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // do nothing
            e.printStackTrace();
        }
        return fieldObj;
    }

    public void invokeSetMethod(Object entityObj, Object fieldObj) {
        try {
            this.getSetMethod().invoke(entityObj, fieldObj);
        } catch (IllegalAccessException e) {
            // do nothing
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // do nothing
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // do nothing
            e.printStackTrace();
        }
    }

    /**
     * @return 字段是否为Boolean boolean类型
     */
    public boolean isBooleanType() {
        if (booleanType == BOOL.UNINITLIZED) {
            booleanType = this.field.getType().equals(Boolean.class)
                          || this.field.getType().equals(boolean.class) ? BOOL.TRUE : BOOL.FALSE;
        }
        return booleanType == BOOL.TRUE;
    }

    /**
     * @return 字段是否为java.util.Date类型
     */
    public boolean isDateType() {
        if (DateType == BOOL.UNINITLIZED) {
            DateType = this.field.getType().equals(java.util.Date.class) ? BOOL.TRUE : BOOL.FALSE;
        }
        return DateType == BOOL.TRUE;
    }

    /**
     * @return @DBAI 指定的是否为自增字段
     */
    public boolean isDBAI() {
        if (dBAI == BOOL.UNINITLIZED) {
            dBAI = this.field.getAnnotation(DBAI.class) != null ? BOOL.TRUE : BOOL.FALSE;
        }
        return dBAI == BOOL.TRUE;
    }

    /**
     * 
     * @return @NotDBField 指定的是否为数据库字段
     */
    public boolean isDBField() {
        if (dBField == BOOL.UNINITLIZED) {
            dBField = this.field.getAnnotation(NotDBField.class) == null ? BOOL.TRUE : BOOL.FALSE;
        }
        return dBField == BOOL.TRUE;
    }

    /**
     * 
     * @return @DBId 指定的是否为ID
     */
    public boolean isDBId() {
        if (dBId == BOOL.UNINITLIZED) {
            dBId = this.field.getAnnotation(DBId.class) != null ? BOOL.TRUE : BOOL.FALSE;
        }
        return dBId == BOOL.TRUE;
    }

    /**
     * 
     * @return @DBName 指定的是否为Name
     */
    public boolean isDBName() {
        if (dBName == BOOL.UNINITLIZED) {
            dBName = this.field.getAnnotation(DBName.class) != null ? BOOL.TRUE : BOOL.FALSE;
        }
        return dBName == BOOL.TRUE;
    }

    /**
     * 
     * @return @DBPK 指定的是否为PK
     */
    public boolean isDBPK() {
        if (dBPK == BOOL.UNINITLIZED) {
            dBPK = this.field.getAnnotation(DBPK.class) != null ? BOOL.TRUE : BOOL.FALSE;
        }
        return dBPK == BOOL.TRUE;
    }
}