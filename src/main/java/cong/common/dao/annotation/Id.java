package cong.common.dao.annotation;

import java.lang.annotation.*;

/**
 * Created by cong on 2014/12/9.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Id {
    String value() default "";
}