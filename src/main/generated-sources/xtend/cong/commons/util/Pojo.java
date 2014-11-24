package cong.commons.util;

import cong.commons.util.PojoProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 */
@Target(ElementType.TYPE)
@Active(PojoProcessor.class)
public @interface Pojo {
}
