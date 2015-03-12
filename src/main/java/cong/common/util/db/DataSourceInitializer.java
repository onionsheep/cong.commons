package cong.common.util.db;

import javax.sql.DataSource;

/**
 * Created by cong on 2015/3/11.
 */
public interface DataSourceInitializer {
    DataSource createDataSource();
}
