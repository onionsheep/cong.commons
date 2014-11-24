/**
 * 创建 @ 2013年7月16日 下午4:26:27
 * 
 */
package cong.commons.lang;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *
 */
public class StringHelper {

  public static String firstToUpperCase(String str){
    String first = str.substring(0, 1);
    String remain = str.substring(1);
    return first.toUpperCase() + remain;
  }
  
  /**
   * 检查字符串是否为null或者长度为0
   * @param str
   * @return
   */
  public static boolean isEmptyOrNull(String str){
    if(str == null){
      return true;
    }
    
    if(str.length() == 0){
      return true;
    }
    
    if("".equals(str)){
      return true;
    }
    return false;
  }
  
  /**
   * 检查字符串是否是空白字符串（全部是空格，或者长度为零）或者null
   * @param str
   * @return
   */
  public static boolean isBlank(String str){
    if(isEmptyOrNull(str)){
      return true;
    }
    if(isEmptyOrNull(str.trim())){
      return true;
    }
    return false;
  }
}
