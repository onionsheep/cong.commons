/**
 * 创建 @ 2013年7月24日 上午10:33:43
 * 
 */
package cong.commons.type;

import jodd.typeconverter.TypeConverterManager;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class JoddConverter implements Converter {

    @Override
    public <T> T convert(Object obj, Class<T> clazz) {
        return TypeConverterManager.convertType(obj, clazz);
    }

}
