package cong.common.dao.sqlcache;

import jodd.util.StringUtil;

/**
 * Created by cong on 2014/10/22.
 */
public class OriginalNameTableNameMaker implements TableNameMaker {
  public String getTableNameFromClass(String tableNamePrefix, Class<?> clazz) {
    return StringUtil.uncapitalize(tableNamePrefix + clazz.getSimpleName());
  }
}
