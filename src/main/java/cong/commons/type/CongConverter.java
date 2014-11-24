/**
 * 创建 @ 2013年7月24日 上午10:17:19
 * 
 */
package cong.commons.type;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class CongConverter implements Converter{

    @Override
    public <T> T convert(Object obj, Class<T> clazz) {
        T t = null;
        if (obj == null) {
            return t;
        }
        if (obj.getClass().equals(clazz)) {
            t = clazz.cast(obj);
        } else if (obj instanceof String) {
            String s = (String) obj;
            if (Integer.class.equals(clazz)) {
                try {
                    t = clazz.cast(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
