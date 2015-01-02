package cong.common.dao.sqlcache;

/**
 * Created by cong on 2014/10/22.
 */
public class LowerCaseTableNameMaker implements TableNameMaker {
    public String getTableNameFromClass(String tableNamePrefix, Class<?> clazz) {
        return tableNamePrefix + clazz.getSimpleName().toLowerCase();
    }

}
