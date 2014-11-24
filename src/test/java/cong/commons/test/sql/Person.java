/**
 * 创建 @ 2013年7月16日 下午4:07:23
 * 
 */
package cong.commons.test.sql;

import cong.commons.sql.DBFieldName;
import cong.commons.sql.NotDBField;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *
 */
public class Person {

  @DBFieldName("name")
  private String xingming;
  private int age;
  @NotDBField 
  private String gender;
  /**
   * @return the xingming
   */
  public String getXingming() {
    return this.xingming;
  }
  /**
   * @param xingming the xingming to set
   */
  public void setXingming(String xingming) {
    this.xingming = xingming;
  }
  /**
   * @return the age
   */
  public int getAge() {
    return this.age;
  }
  /**
   * @param age the age to set
   */
  public void setAge(int age) {
    this.age = age;
  }
  /**
   * @return the gender
   */
  public String getGender() {
    return this.gender;
  }
  /**
   * @param gender the gender to set
   */
  public void setGender(String gender) {
    this.gender = gender;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Person : {xingming:");
    builder.append(this.xingming);
    builder.append(", age:");
    builder.append(this.age);
    builder.append(", gender:");
    builder.append(this.gender);
    builder.append("}");
    return builder.toString();
  }

  
 
}
