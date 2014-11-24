package cong.commons.util

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *
 */
@Target(ElementType::TYPE)
@Active(typeof(PojoProcessor))
annotation Pojo {
}

class PojoProcessor extends AbstractClassProcessor {

	//	override doRegisterGlobals(ClassDeclaration annotatedClass, RegisterGlobalsContext context) {
	//		context.registerInterface(annotatedClass.interfaceName)
	//	}
	//
	//	def getInterfaceName(ClassDeclaration annotatedClass) {
	//		annotatedClass.qualifiedName + "Interface"
	//	}
	val DEFAULT_STRING_BUILDER_SIZE = 1024;

	override doTransform(MutableClassDeclaration clazz, extension TransformationContext context) {

		//包含所有字段的构造函数
		if (clazz.declaredFields.iterator.hasNext) {
			var paramterTypes = newArrayList();
			for (f : clazz.declaredFields) {
				if (!f.static) {
					paramterTypes.add(f.type)
				}
			}
			if (clazz.findDeclaredConstructor(paramterTypes) == null) {
				clazz.addConstructor() [
					val sb = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE);
					for (f : clazz.declaredFields) {
						if (!f.static) {
							addParameter(f.simpleName, f.type);
							sb.append(
								'''
									this.«f.simpleName» = «f.simpleName»;
								''');
						}
					}
					sb.deleteCharAt(sb.length() - 1)
					body = [sb.toString]
				]
			}
		}

		//无参构造函数
		if (clazz.findDeclaredConstructor(newArrayList()) == null) {
			clazz.addConstructor() [
				body = ['']
			]
		}

		/**
		 * 添加toString方法
		 */
		if (clazz.findDeclaredMethod("toString") == null) {
			clazz.addMethod('toString') [
				returnType = context.newTypeReference(typeof(String))
				val sb = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE)
				sb.append('StringBuilder sb = new StringBuilder();')
				sb.append('\n')
				sb.append('''sb.append("«clazz.simpleName» : {''')
				var once = true
				for (f : clazz.declaredFields) {
					if (once) {
						sb.append(''' «f.simpleName» : ");''')
						once = false
					} else {
						sb.append('''sb.append(", «f.simpleName» : ");''')
					}
					sb.append('\n')
					sb.append('''sb.append(«f.simpleName»);''')
					sb.append('\n')
				}
				if (once) {
					sb.append(' ");\n')
				}
				sb.append("sb.append('}');\n")
				sb.append('return sb.toString();')
				body = ['''«sb.toString»''']
			]
		}
		/**
		 * 添加equals方法
		 */
		if (clazz.findDeclaredMethod("equals") == null) {
			clazz.addMethod('equals') [
				returnType = context.newTypeReference(typeof(boolean))
				val sb = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE)
				sb.append(
					'''
						if (this == obj) {
							return true;
						}
						if (obj == null) {
							return false;
						}
						if (this.getClass() != obj.getClass()) {
							return false;
						}
					''')
				val clazzSimpleName = clazz.simpleName
				sb.append(
					'''
						final «clazzSimpleName» other = («clazzSimpleName») obj;
					''')
				for (field : clazz.declaredFields) {
					val fieldType = field.getType
					if (fieldType.isPrimitive()) {
						sb.append(
							'''
								if (this.«field.simpleName» != other.«field.simpleName») {
									return false;
								}
							''')
					} else {
						sb.append(
							'''
								if (this.«field.simpleName» == null) {
									if (other.«field.simpleName» != null) {
										return false;
									}
								} else if (!this.«field.simpleName».equals(other.«field.simpleName»)) {
									return false;
								}
							''')
					}
				}
				sb.append("return true;")
				body = ['''«sb.toString»''']
			].addParameter('obj', context.newTypeReference(typeof(Object)))
		}

		/**
		 * 添加hashCode方法
		 */
		if (clazz.findDeclaredMethod("hashCode") == null) {
			clazz.addMethod('hashCode') [
				returnType = context.newTypeReference(typeof(int))
				val sb = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE);
				sb.append(
					'''
						final int prime = 31;
						int result = 1;
					''')
				for (field : clazz.declaredFields) {
					val fieldType = field.getType
					if (fieldType.isPrimitive()) {
						val fieldTypeName = fieldType.simpleName;
						switch fieldTypeName {
							case "boolean":
								sb.append(
									'''
										result = prime * result + (this.«field.simpleName» ? 1231 : 1237);
									''')
							case "char":
								sb.append(
									'''result = prime * result + this.«field.simpleName»;
										''')
							case "double": {
								sb.append(
									'''
										long temp;
										temp = Double.doubleToLongBits(this.«field.simpleName»);
										result = prime * result + (int) (temp ^ (temp >>> 32));
									''')
							}
							case "float":
								sb.append(
									'''
										result = prime * result + Float.floatToIntBits(this.«field.simpleName»);
									''')
							case "int":
								sb.append(
									'''
										result = prime * result + this.«field.simpleName»;
									''')
							case "long":
								sb.append(
									'''
										result = prime * result + (int) (this.«field.simpleName» ^ (this.«field.simpleName» >>> 32));
									''')
						}
					} else {
						sb.append(
							'''
								result = prime * result + ((this.«field.simpleName» == null) ? 0 : this.«field.simpleName».hashCode());
							''')
					}
				}
				sb.append(
					'''
						return result;
					''')
				body = ['''«sb.toString»''']
			]

		}

		/**
		 * 为每个字段添加get
		 */
		clazz.declaredFields.filter [ field |
			!field.static
		].forEach [ field |
			val fieldName = field.simpleName
			clazz.addMethod('get' + fieldName.toFirstUpper) [
				returnType = field.type
				body = ['''return this.«fieldName»;''']
			]
		]

		/**
		 * 为每个字段添加set
		 */
		clazz.declaredFields.filter [ field |
			!field.static && !field.final
		].forEach [ field |
			val fieldName = field.simpleName
			clazz.addMethod('set' + fieldName.toFirstUpper) [
				addParameter(fieldName, field.type)
				body = ['''this.«fieldName» = «fieldName»;''']
			]
		]

	}

}
