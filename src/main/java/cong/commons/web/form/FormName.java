/**
 * 创建 @ 2013年7月23日 下午9:40:26
 * 
 */
package cong.commons.web.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormName {
    String value() default "";
}
