package cong.common.dao.sqlcache;

import jodd.util.StringUtil;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cong on 8/13 0013.
 */
public class OriginalNameFieldColumnMapMaker implements FieldColumnMapMaker {

  @Override
  public ConcurrentHashMap<String, String> makeFieldColumnMap(Field[] fields, ConcurrentHashMap<String, String> mapToMake) {
    if (mapToMake == null) {
      mapToMake = new ConcurrentHashMap<String, String>();
    }
    for (Field field : fields) {
      String fieldName = field.getName();
      if (StringUtil.isBlank(mapToMake.get(fieldName))) {
        mapToMake.put(fieldName, fieldName);
      }
    }
    return mapToMake;
  }

  @Override
  public ConcurrentHashMap<String, String> makeFieldColumnMap(Field[] fields) {
    return makeFieldColumnMap(fields, null);
  }

}
