/**
 * 创建 @ 2013年7月23日 下午9:37:51
 * 
 */
package cong.commons.web.form;

import java.lang.reflect.Field;

import cong.commons.bean.BeanField;
import cong.commons.lang.StringHelper;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class FormBeanField extends BeanField {

    public FormBeanField(Field field) {
        super(field);
    }

    public FormBeanField(BeanField beanField) {
        super(beanField.getField());
    }

    protected String formName = null;

    public String getFormName() {
        this.initFormName();
        return this.formName;
    }

    /**
     * 优先使用注解FormName，否则使用字段名
     */
    protected void initFormName() {
        if (this.formName == null) {
            FormName formNameAnno = this.field.getAnnotation(FormName.class);
            if (formNameAnno != null && !StringHelper.isBlank(formNameAnno.value())) {
                this.formName = formNameAnno.value();
            } else {
                this.formName = field.getName();
            }
        }
    }

}
