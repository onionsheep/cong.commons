/**
 * 创建 @ 2013年7月23日 下午9:49:56
 * 
 */
package cong.commons.web.form;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cong.commons.bean.BeanClass;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * @param <T>
 * 
 */
public class FormBeanClass<T> extends BeanClass<T> {

    private ArrayList<FormBeanField> formBeanFieldList;

    public FormBeanClass(Class<T> clazz) {
        super(clazz);
    }

    public ArrayList<FormBeanField> getFormBeanFieldList(){
        initFormBeanFieldList();
        return this.formBeanFieldList;
    }

    protected void initFormBeanFieldList() {
        if(this.formBeanFieldList == null){
            formBeanFieldList = new ArrayList<FormBeanField>();
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                formBeanFieldList.add(new FormBeanField(field));
            }
        }
        
    }
    
    
}
