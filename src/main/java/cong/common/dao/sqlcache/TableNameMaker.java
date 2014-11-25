package cong.common.dao.sqlcache;

/**
 * Created by cong on 2014/10/22.
 */
public interface TableNameMaker {
  public String getTableNameFromClass(String tableNamePrefix, Class<?> clazz);
}
