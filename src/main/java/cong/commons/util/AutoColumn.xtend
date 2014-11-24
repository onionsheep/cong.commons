/**
 * 2013年11月28日 下午2:37:27
 */
package cong.commons.util

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.TransformationContext
import javax.persistence.Column

/**
 * @author cong onion_sheep@163.com|onionsheep@gmail.com
 *
 */
@Target(ElementType::FIELD)
annotation NotAutoColumn {
}

@Target(ElementType::TYPE)
@Active(typeof(AutoColumnProcessor))
annotation AutoColumn {
}

class AutoColumnProcessor extends AbstractClassProcessor {
	override doTransform(MutableClassDeclaration clazz, extension TransformationContext context) {

		/**
		 * 为每个字段添加@Column(name="col_name"),其中col_name是根据驼峰式字段名自动生成的，例如entityId => entity_id
		 * 若要避免自动生成此注解，可添加@NotAutoColumn
		 * */
		clazz.declaredFields.forEach [ field |
			val columnAnnotationType = context.newTypeReference(typeof(Column)).getType
			val existeColumndAnno = field.findAnnotation(columnAnnotationType)
			val notColumnAnnotationType = context.newTypeReference(typeof(NotAutoColumn)).getType
			val existeNotColumndAnno = field.findAnnotation(notColumnAnnotationType)
			if (existeColumndAnno == null && existeNotColumndAnno == null) {
				field.addAnnotation(columnAnnotationType)
				val muanno = field.findAnnotation(columnAnnotationType)
				muanno.setStringValue("name", this.getColumnNameFromFieldName(field.simpleName))
			}
			if (existeNotColumndAnno != null) {
			}
		]
	}

	/**
	 * @param name 驼峰式命名的字符串
	 * @return 把驼峰式命名的字符串改成下划线命名，例如entityId => entity_id
	 */
	def getColumnNameFromFieldName(String name) {
		val StringBuilder sb = new StringBuilder()
		val nameCharArray = name.toCharArray()
		for (char c : nameCharArray) {
			if (c >= 'A' && c <= 'Z') {
				sb.append("_")
				sb.append(Character.toLowerCase(c))
			} else {
				sb.append(c)
			}
		}
		return sb.toString()
	}
}
