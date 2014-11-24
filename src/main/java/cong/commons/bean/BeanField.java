/**
 * 创建 @ 2013年7月23日 下午8:39:28
 * 
 */
package cong.commons.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cong.commons.lang.StringHelper;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class BeanField {

    protected Class<?> clazz;

    protected Field    field;

    protected Class<?> fieldClazz;
    
    protected Method   getMethod;
    protected Method   setMethod;

    public BeanField(Field field) {
        this.field = field;
    }

    public Class<?> getDeclaringClass() {
        initDeclaringClass();
        return this.clazz;
    }

    /**
     * @return the field
     */
    public Field getField() {
        return this.field;
    }

    public Class<?> getFieldClass(){
        initFieldClass();
        return this.fieldClazz;
    }
    
    protected void initFieldClass(){
        if(this.fieldClazz == null){
            this.fieldClazz = field.getClass();
        }
    }
    
    /**
     * @return the getMethod
     */
    public Method getGetMethod() {
        initGetMethod();
        return this.getMethod;
    }

    /**
     * @return the setMethod
     */
    public Method getSetMethod() {
        initSetMethod();
        return this.setMethod;
    }

    protected void initDeclaringClass() {
        if (this.clazz == null) {
            this.clazz = this.field.getDeclaringClass();
        }
    }

    protected void initGetMethod() {
        if (this.getMethod == null) {
            initDeclaringClass();
            try {
                this.getMethod = clazz.getMethod("get"
                                                 + StringHelper.firstToUpperCase(this.field.getName()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    protected void initSetMethod() {
        if (this.setMethod == null) {
            initDeclaringClass();
            try {
                this.setMethod = clazz.getMethod("set"
                                                 + StringHelper.firstToUpperCase(this.field.getName()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public Object invokeGetMethod(Object entity) {
        initDeclaringClass();
        if (!this.clazz.equals(entity.getClass())) {
            return null;
        }
        this.initGetMethod();

        Object obj = null;
        try {
            obj = getMethod.invoke(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void invokeSetMethod(Object entity, Object fieldValue) {
        this.initSetMethod();
        try {
            setMethod.invoke(entity, fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
