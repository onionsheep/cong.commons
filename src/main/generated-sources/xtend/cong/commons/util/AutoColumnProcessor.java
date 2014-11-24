/**
 * 2013年11月28日 下午2:37:27
 */
package cong.commons.util;

import com.google.common.base.Objects;
import cong.commons.util.NotAutoColumn;
import javax.persistence.Column;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.MutableAnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Type;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AutoColumnProcessor extends AbstractClassProcessor {
  public void doTransform(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
    final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration field) {
        TypeReference _newTypeReference = context.newTypeReference(Column.class);
        final Type columnAnnotationType = _newTypeReference.getType();
        final MutableAnnotationReference existeColumndAnno = field.findAnnotation(columnAnnotationType);
        TypeReference _newTypeReference_1 = context.newTypeReference(NotAutoColumn.class);
        final Type notColumnAnnotationType = _newTypeReference_1.getType();
        final MutableAnnotationReference existeNotColumndAnno = field.findAnnotation(notColumnAnnotationType);
        boolean _and = false;
        boolean _equals = Objects.equal(existeColumndAnno, null);
        if (!_equals) {
          _and = false;
        } else {
          boolean _equals_1 = Objects.equal(existeNotColumndAnno, null);
          _and = (_equals && _equals_1);
        }
        if (_and) {
          field.addAnnotation(columnAnnotationType);
          final MutableAnnotationReference muanno = field.findAnnotation(columnAnnotationType);
          String _simpleName = field.getSimpleName();
          String _columnNameFromFieldName = AutoColumnProcessor.this.getColumnNameFromFieldName(_simpleName);
          muanno.setStringValue("name", _columnNameFromFieldName);
        }
        boolean _notEquals = (!Objects.equal(existeNotColumndAnno, null));
        if (_notEquals) {
        }
      }
    };
    IterableExtensions.forEach(_declaredFields, _function);
  }
  
  /**
   * @param name 驼峰式命名的字符串
   * @return 把驼峰式命名的字符串改成下划线命名，例如entityId => entity_id
   */
  public String getColumnNameFromFieldName(final String name) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    final char[] nameCharArray = name.toCharArray();
    for (final char c : nameCharArray) {
      boolean _and = false;
      if (!(c >= 'A')) {
        _and = false;
      } else {
        _and = ((c >= 'A') && (c <= 'Z'));
      }
      if (_and) {
        sb.append("_");
        char _lowerCase = Character.toLowerCase(c);
        sb.append(_lowerCase);
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }
}
