/**
 * 创建 @ 2013年7月24日 上午10:32:36
 * 
 */
package cong.commons.type;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 *
 */
public interface Converter {

    public <T> T convert(Object obj, Class<T> clazz);
}
