/**
 * 创建 @ 2013年7月17日 下午4:32:34
 * 
 */
package cong.commons.test.sql;

import cong.commons.sql.DBAI;
import cong.commons.sql.DBFieldName;
import cong.commons.sql.DBId;
import cong.commons.sql.DBName;
import cong.commons.sql.DBPK;
import cong.commons.sql.DBTable;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *
 */
@DBTable("t_entity0")
public class Entity0 {
  @DBId 
  @DBAI
  private int id;
  @DBPK
  @DBFieldName("intege")
  private int integer;
  private double doubl;
  @DBPK
  private int byt;
  private boolean bool;
  @DBName
  private String string;
  
  
  
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Entity0 : {id:");
    builder.append(this.id);
    builder.append(", integer:");
    builder.append(this.integer);
    builder.append(", doubl:");
    builder.append(this.doubl);
    builder.append(", byt:");
    builder.append(this.byt);
    builder.append(", bool:");
    builder.append(this.bool);
    builder.append(", string:");
    builder.append(this.string);
    builder.append("}");
    return builder.toString();
  }
  /**
   * @return the id
   */
  public int getId() {
    return this.id;
  }
  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  /**
   * @return the integer
   */
  public int getInteger() {
    return this.integer;
  }
  /**
   * @param integer the integer to set
   */
  public void setInteger(int integer) {
    this.integer = integer;
  }
  /**
   * @return the doubl
   */
  public double getDoubl() {
    return this.doubl;
  }
  /**
   * @param doubl the doubl to set
   */
  public void setDoubl(double doubl) {
    this.doubl = doubl;
  }
  /**
   * @return the byt
   */
  public int getByt() {
    return this.byt;
  }
  /**
   * @param byt the byt to set
   */
  public void setByt(int byt) {
    this.byt = byt;
  }
  /**
   * @return the bool
   */
  public boolean isBool() {
    return this.bool;
  }
  /**
   * @param bool the bool to set
   */
  public void setBool(boolean bool) {
    this.bool = bool;
  }
  /**
   * @return the string
   */
  public String getString() {
    return this.string;
  }
  /**
   * @param string the string to set
   */
  public void setString(String string) {
    this.string = string;
  }
  
  
}
