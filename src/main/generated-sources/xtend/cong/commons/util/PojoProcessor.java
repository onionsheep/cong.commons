package cong.commons.util;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableConstructorDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class PojoProcessor extends AbstractClassProcessor {
  private final int DEFAULT_STRING_BUILDER_SIZE = 1024;
  
  public void doTransform(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
    Iterator<? extends MutableFieldDeclaration> _iterator = _declaredFields.iterator();
    boolean _hasNext = _iterator.hasNext();
    if (_hasNext) {
      ArrayList<TypeReference> paramterTypes = CollectionLiterals.<TypeReference>newArrayList();
      Iterable<? extends MutableFieldDeclaration> _declaredFields_1 = clazz.getDeclaredFields();
      for (final MutableFieldDeclaration f : _declaredFields_1) {
        boolean _isStatic = f.isStatic();
        boolean _not = (!_isStatic);
        if (_not) {
          TypeReference _type = f.getType();
          paramterTypes.add(_type);
        }
      }
      final ArrayList<TypeReference> _converted_paramterTypes = (ArrayList<TypeReference>)paramterTypes;
      MutableConstructorDeclaration _findDeclaredConstructor = clazz.findDeclaredConstructor(((TypeReference[])Conversions.unwrapArray(_converted_paramterTypes, TypeReference.class)));
      boolean _equals = Objects.equal(_findDeclaredConstructor, null);
      if (_equals) {
        final Procedure1<MutableConstructorDeclaration> _function = new Procedure1<MutableConstructorDeclaration>() {
          public void apply(final MutableConstructorDeclaration it) {
            StringBuilder _stringBuilder = new StringBuilder(PojoProcessor.this.DEFAULT_STRING_BUILDER_SIZE);
            final StringBuilder sb = _stringBuilder;
            Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
            for (final MutableFieldDeclaration f : _declaredFields) {
              boolean _isStatic = f.isStatic();
              boolean _not = (!_isStatic);
              if (_not) {
                String _simpleName = f.getSimpleName();
                TypeReference _type = f.getType();
                it.addParameter(_simpleName, _type);
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("this.");
                String _simpleName_1 = f.getSimpleName();
                _builder.append(_simpleName_1, "");
                _builder.append(" = ");
                String _simpleName_2 = f.getSimpleName();
                _builder.append(_simpleName_2, "");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                sb.append(_builder);
              }
            }
            int _length = sb.length();
            int _minus = (_length - 1);
            sb.deleteCharAt(_minus);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                String _string = sb.toString();
                return _string;
              }
            };
            it.setBody(_function);
          }
        };
        clazz.addConstructor(_function);
      }
    }
    ArrayList<TypeReference> _newArrayList = CollectionLiterals.<TypeReference>newArrayList();
    MutableConstructorDeclaration _findDeclaredConstructor_1 = clazz.findDeclaredConstructor(((TypeReference[])Conversions.unwrapArray(_newArrayList, TypeReference.class)));
    boolean _equals_1 = Objects.equal(_findDeclaredConstructor_1, null);
    if (_equals_1) {
      final Procedure1<MutableConstructorDeclaration> _function_1 = new Procedure1<MutableConstructorDeclaration>() {
        public void apply(final MutableConstructorDeclaration it) {
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              return "";
            }
          };
          it.setBody(_function);
        }
      };
      clazz.addConstructor(_function_1);
    }
    MutableMethodDeclaration _findDeclaredMethod = clazz.findDeclaredMethod("toString");
    boolean _equals_2 = Objects.equal(_findDeclaredMethod, null);
    if (_equals_2) {
      final Procedure1<MutableMethodDeclaration> _function_2 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          TypeReference _newTypeReference = context.newTypeReference(String.class);
          it.setReturnType(_newTypeReference);
          StringBuilder _stringBuilder = new StringBuilder(PojoProcessor.this.DEFAULT_STRING_BUILDER_SIZE);
          final StringBuilder sb = _stringBuilder;
          sb.append("StringBuilder sb = new StringBuilder();");
          sb.append("\n");
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("sb.append(\"");
          String _simpleName = clazz.getSimpleName();
          _builder.append(_simpleName, "");
          _builder.append(" : {");
          sb.append(_builder);
          boolean once = true;
          Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
          for (final MutableFieldDeclaration f : _declaredFields) {
            {
              if (once) {
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append(" ");
                String _simpleName_1 = f.getSimpleName();
                _builder_1.append(_simpleName_1, " ");
                _builder_1.append(" : \");");
                sb.append(_builder_1);
                once = false;
              } else {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("sb.append(\", ");
                String _simpleName_2 = f.getSimpleName();
                _builder_2.append(_simpleName_2, "");
                _builder_2.append(" : \");");
                sb.append(_builder_2);
              }
              sb.append("\n");
              StringConcatenation _builder_3 = new StringConcatenation();
              _builder_3.append("sb.append(");
              String _simpleName_3 = f.getSimpleName();
              _builder_3.append(_simpleName_3, "");
              _builder_3.append(");");
              sb.append(_builder_3);
              sb.append("\n");
            }
          }
          if (once) {
            sb.append(" \");\n");
          }
          sb.append("sb.append(\'}\');\n");
          sb.append("return sb.toString();");
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              String _string = sb.toString();
              _builder.append(_string, "");
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      clazz.addMethod("toString", _function_2);
    }
    MutableMethodDeclaration _findDeclaredMethod_1 = clazz.findDeclaredMethod("equals");
    boolean _equals_3 = Objects.equal(_findDeclaredMethod_1, null);
    if (_equals_3) {
      final Procedure1<MutableMethodDeclaration> _function_3 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          TypeReference _newTypeReference = context.newTypeReference(boolean.class);
          it.setReturnType(_newTypeReference);
          StringBuilder _stringBuilder = new StringBuilder(PojoProcessor.this.DEFAULT_STRING_BUILDER_SIZE);
          final StringBuilder sb = _stringBuilder;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("if (this == obj) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("return true;");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          _builder.append("if (obj == null) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("return false;");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          _builder.append("if (this.getClass() != obj.getClass()) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("return false;");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          sb.append(_builder);
          final String clazzSimpleName = clazz.getSimpleName();
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("final ");
          _builder_1.append(clazzSimpleName, "");
          _builder_1.append(" other = (");
          _builder_1.append(clazzSimpleName, "");
          _builder_1.append(") obj;");
          _builder_1.newLineIfNotEmpty();
          sb.append(_builder_1);
          Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
          for (final MutableFieldDeclaration field : _declaredFields) {
            {
              final TypeReference fieldType = field.getType();
              boolean _isPrimitive = fieldType.isPrimitive();
              if (_isPrimitive) {
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("if (this.");
                String _simpleName = field.getSimpleName();
                _builder_2.append(_simpleName, "");
                _builder_2.append(" != other.");
                String _simpleName_1 = field.getSimpleName();
                _builder_2.append(_simpleName_1, "");
                _builder_2.append(") {");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("\t");
                _builder_2.append("return false;");
                _builder_2.newLine();
                _builder_2.append("}");
                _builder_2.newLine();
                sb.append(_builder_2);
              } else {
                StringConcatenation _builder_3 = new StringConcatenation();
                _builder_3.append("if (this.");
                String _simpleName_2 = field.getSimpleName();
                _builder_3.append(_simpleName_2, "");
                _builder_3.append(" == null) {");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("\t");
                _builder_3.append("if (other.");
                String _simpleName_3 = field.getSimpleName();
                _builder_3.append(_simpleName_3, "\t");
                _builder_3.append(" != null) {");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("\t\t");
                _builder_3.append("return false;");
                _builder_3.newLine();
                _builder_3.append("\t");
                _builder_3.append("}");
                _builder_3.newLine();
                _builder_3.append("} else if (!this.");
                String _simpleName_4 = field.getSimpleName();
                _builder_3.append(_simpleName_4, "");
                _builder_3.append(".equals(other.");
                String _simpleName_5 = field.getSimpleName();
                _builder_3.append(_simpleName_5, "");
                _builder_3.append(")) {");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("\t");
                _builder_3.append("return false;");
                _builder_3.newLine();
                _builder_3.append("}");
                _builder_3.newLine();
                sb.append(_builder_3);
              }
            }
          }
          sb.append("return true;");
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              String _string = sb.toString();
              _builder.append(_string, "");
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      MutableMethodDeclaration _addMethod = clazz.addMethod("equals", _function_3);
      TypeReference _newTypeReference = context.newTypeReference(Object.class);
      _addMethod.addParameter("obj", _newTypeReference);
    }
    MutableMethodDeclaration _findDeclaredMethod_2 = clazz.findDeclaredMethod("hashCode");
    boolean _equals_4 = Objects.equal(_findDeclaredMethod_2, null);
    if (_equals_4) {
      final Procedure1<MutableMethodDeclaration> _function_4 = new Procedure1<MutableMethodDeclaration>() {
        public void apply(final MutableMethodDeclaration it) {
          TypeReference _newTypeReference = context.newTypeReference(int.class);
          it.setReturnType(_newTypeReference);
          StringBuilder _stringBuilder = new StringBuilder(PojoProcessor.this.DEFAULT_STRING_BUILDER_SIZE);
          final StringBuilder sb = _stringBuilder;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("final int prime = 31;");
          _builder.newLine();
          _builder.append("int result = 1;");
          _builder.newLine();
          sb.append(_builder);
          Iterable<? extends MutableFieldDeclaration> _declaredFields = clazz.getDeclaredFields();
          for (final MutableFieldDeclaration field : _declaredFields) {
            {
              final TypeReference fieldType = field.getType();
              boolean _isPrimitive = fieldType.isPrimitive();
              if (_isPrimitive) {
                final String fieldTypeName = fieldType.getSimpleName();
                boolean _matched = false;
                if (!_matched) {
                  if (Objects.equal(fieldTypeName,"boolean")) {
                    _matched=true;
                    StringConcatenation _builder_1 = new StringConcatenation();
                    _builder_1.append("result = prime * result + (this.");
                    String _simpleName = field.getSimpleName();
                    _builder_1.append(_simpleName, "");
                    _builder_1.append(" ? 1231 : 1237);");
                    _builder_1.newLineIfNotEmpty();
                    sb.append(_builder_1);
                  }
                }
                if (!_matched) {
                  if (Objects.equal(fieldTypeName,"char")) {
                    _matched=true;
                    StringConcatenation _builder_2 = new StringConcatenation();
                    _builder_2.append("result = prime * result + this.");
                    String _simpleName_1 = field.getSimpleName();
                    _builder_2.append(_simpleName_1, "");
                    _builder_2.append(";");
                    _builder_2.newLineIfNotEmpty();
                    sb.append(_builder_2);
                  }
                }
                if (!_matched) {
                  if (Objects.equal(fieldTypeName,"double")) {
                    _matched=true;
                    StringConcatenation _builder_3 = new StringConcatenation();
                    _builder_3.append("long temp;");
                    _builder_3.newLine();
                    _builder_3.append("temp = Double.doubleToLongBits(this.");
                    String _simpleName_2 = field.getSimpleName();
                    _builder_3.append(_simpleName_2, "");
                    _builder_3.append(");");
                    _builder_3.newLineIfNotEmpty();
                    _builder_3.append("result = prime * result + (int) (temp ^ (temp >>> 32));");
                    _builder_3.newLine();
                    sb.append(_builder_3);
                  }
                }
                if (!_matched) {
                  if (Objects.equal(fieldTypeName,"float")) {
                    _matched=true;
                    StringConcatenation _builder_4 = new StringConcatenation();
                    _builder_4.append("result = prime * result + Float.floatToIntBits(this.");
                    String _simpleName_3 = field.getSimpleName();
                    _builder_4.append(_simpleName_3, "");
                    _builder_4.append(");");
                    _builder_4.newLineIfNotEmpty();
                    sb.append(_builder_4);
                  }
                }
                if (!_matched) {
                  if (Objects.equal(fieldTypeName,"int")) {
                    _matched=true;
                    StringConcatenation _builder_5 = new StringConcatenation();
                    _builder_5.append("result = prime * result + this.");
                    String _simpleName_4 = field.getSimpleName();
                    _builder_5.append(_simpleName_4, "");
                    _builder_5.append(";");
                    _builder_5.newLineIfNotEmpty();
                    sb.append(_builder_5);
                  }
                }
                if (!_matched) {
                  if (Objects.equal(fieldTypeName,"long")) {
                    _matched=true;
                    StringConcatenation _builder_6 = new StringConcatenation();
                    _builder_6.append("result = prime * result + (int) (this.");
                    String _simpleName_5 = field.getSimpleName();
                    _builder_6.append(_simpleName_5, "");
                    _builder_6.append(" ^ (this.");
                    String _simpleName_6 = field.getSimpleName();
                    _builder_6.append(_simpleName_6, "");
                    _builder_6.append(" >>> 32));");
                    _builder_6.newLineIfNotEmpty();
                    sb.append(_builder_6);
                  }
                }
              } else {
                StringConcatenation _builder_7 = new StringConcatenation();
                _builder_7.append("result = prime * result + ((this.");
                String _simpleName_7 = field.getSimpleName();
                _builder_7.append(_simpleName_7, "");
                _builder_7.append(" == null) ? 0 : this.");
                String _simpleName_8 = field.getSimpleName();
                _builder_7.append(_simpleName_8, "");
                _builder_7.append(".hashCode());");
                _builder_7.newLineIfNotEmpty();
                sb.append(_builder_7);
              }
            }
          }
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("return result;");
          _builder_1.newLine();
          sb.append(_builder_1);
          final CompilationStrategy _function = new CompilationStrategy() {
            public CharSequence compile(final CompilationStrategy.CompilationContext it) {
              StringConcatenation _builder = new StringConcatenation();
              String _string = sb.toString();
              _builder.append(_string, "");
              return _builder;
            }
          };
          it.setBody(_function);
        }
      };
      clazz.addMethod("hashCode", _function_4);
    }
    Iterable<? extends MutableFieldDeclaration> _declaredFields_2 = clazz.getDeclaredFields();
    final Function1<MutableFieldDeclaration,Boolean> _function_5 = new Function1<MutableFieldDeclaration,Boolean>() {
      public Boolean apply(final MutableFieldDeclaration field) {
        boolean _isStatic = field.isStatic();
        boolean _not = (!_isStatic);
        return Boolean.valueOf(_not);
      }
    };
    Iterable<? extends MutableFieldDeclaration> _filter = IterableExtensions.filter(_declaredFields_2, _function_5);
    final Procedure1<MutableFieldDeclaration> _function_6 = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration field) {
        final String fieldName = field.getSimpleName();
        String _firstUpper = StringExtensions.toFirstUpper(fieldName);
        String _plus = ("get" + _firstUpper);
        final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
          public void apply(final MutableMethodDeclaration it) {
            TypeReference _type = field.getType();
            it.setReturnType(_type);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("return this.");
                _builder.append(fieldName, "");
                _builder.append(";");
                return _builder;
              }
            };
            it.setBody(_function);
          }
        };
        clazz.addMethod(_plus, _function);
      }
    };
    IterableExtensions.forEach(_filter, _function_6);
    Iterable<? extends MutableFieldDeclaration> _declaredFields_3 = clazz.getDeclaredFields();
    final Function1<MutableFieldDeclaration,Boolean> _function_7 = new Function1<MutableFieldDeclaration,Boolean>() {
      public Boolean apply(final MutableFieldDeclaration field) {
        boolean _and = false;
        boolean _isStatic = field.isStatic();
        boolean _not = (!_isStatic);
        if (!_not) {
          _and = false;
        } else {
          boolean _isFinal = field.isFinal();
          boolean _not_1 = (!_isFinal);
          _and = (_not && _not_1);
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<? extends MutableFieldDeclaration> _filter_1 = IterableExtensions.filter(_declaredFields_3, _function_7);
    final Procedure1<MutableFieldDeclaration> _function_8 = new Procedure1<MutableFieldDeclaration>() {
      public void apply(final MutableFieldDeclaration field) {
        final String fieldName = field.getSimpleName();
        String _firstUpper = StringExtensions.toFirstUpper(fieldName);
        String _plus = ("set" + _firstUpper);
        final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
          public void apply(final MutableMethodDeclaration it) {
            TypeReference _type = field.getType();
            it.addParameter(fieldName, _type);
            final CompilationStrategy _function = new CompilationStrategy() {
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("this.");
                _builder.append(fieldName, "");
                _builder.append(" = ");
                _builder.append(fieldName, "");
                _builder.append(";");
                return _builder;
              }
            };
            it.setBody(_function);
          }
        };
        clazz.addMethod(_plus, _function);
      }
    };
    IterableExtensions.forEach(_filter_1, _function_8);
  }
}
