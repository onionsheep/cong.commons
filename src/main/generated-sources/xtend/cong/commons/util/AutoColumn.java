/**
 * 2013年11月28日 下午2:37:27
 */
package cong.commons.util;

import cong.commons.util.AutoColumnProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

@Target(ElementType.TYPE)
@Active(AutoColumnProcessor.class)
public @interface AutoColumn {
}
