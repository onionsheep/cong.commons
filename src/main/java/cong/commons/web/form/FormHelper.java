/**
 * 创建 @ 2013年7月23日 下午8:28:44
 * 
 */
package cong.commons.web.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cong.commons.type.Converter;
import cong.commons.type.JoddConverter;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class FormHelper {

    public <T> T formToBean(Class<T> clazz, HttpServletRequest request) {
        T t = null;
        try {
            t = clazz.newInstance();
            FormBeanClass<T> formBeanClass = new FormBeanClass<T>(clazz);
            List<FormBeanField> formBeanFieldList = formBeanClass.getFormBeanFieldList();
            for (FormBeanField formBeanField : formBeanFieldList) {
                String formName = formBeanField.getFormName();
                String fieldStringValue = request.getParameter(formName);
                if (fieldStringValue == null) {
                    continue;
                }
                Class<?> fieldType = formBeanField.getFieldClass();
                Converter converter = new JoddConverter();
                Object fieldValue = converter.convert(fieldStringValue, fieldType);
                formBeanField.invokeSetMethod(t, fieldValue);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}
