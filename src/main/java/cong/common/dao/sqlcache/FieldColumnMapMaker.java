package cong.common.dao.sqlcache;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cong on 8/13 0013.
 */
public interface FieldColumnMapMaker {
    public ConcurrentHashMap<String, String> makeFieldColumnMap(Field[] fields, ConcurrentHashMap<String, String> mapToMake);

    public ConcurrentHashMap<String, String> makeFieldColumnMap(Field[] fields);
}
