/**
 * 创建 @ 2013年7月17日 下午7:48:58
 * 
 */
package cong.commons.sql;

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
public @interface DBAI {
  String value() default "";
}
