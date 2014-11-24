/**
 * 创建 @ 2013年7月23日 下午8:39:19
 * 
 */
package cong.commons.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class BeanClass<T> {
    protected Class<T>             clazz;

    protected ArrayList<BeanField> beanFieldList;

    public BeanClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public ArrayList<BeanField> getBeanFieldList() {
        initBeanFieldList();
        return this.beanFieldList;
    }

    protected void initBeanFieldList() {
        if (this.beanFieldList == null) {
            beanFieldList = new ArrayList<BeanField>();
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                beanFieldList.add(new BeanField(field));
            }
        }
    }
}
