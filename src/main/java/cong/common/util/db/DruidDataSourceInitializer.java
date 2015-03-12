package cong.common.util.db;

import com.alibaba.druid.pool.DruidDataSource;
import cong.common.util.Config;
import jodd.props.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by cong on 2015/3/11.
 */
public class DruidDataSourceInitializer implements DataSourceInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(DruidDataSourceInitializer.class);

    public static final int DEFAULT_MAX_ACTIVE = 32;
    public static final int DEFAULT_INITIAL_SIZE = 1;
    public static final int DEFAULT_MIN_IDLE = 1;

    public DataSource createDataSource() {
        DruidDataSource ds = new DruidDataSource();
        final Props props = Config.load();
        ds.setDriverClassName(props.getValue("db.driver"));
        ds.setUrl(props.getValue("db.url"));
        ds.setUsername(props.getValue("db.username"));
        ds.setPassword(props.getValue("db.password"));
        final String maxActiveSrting = props.getValue("db.maxActive");
        final String initialSizeString = props.getValue("db.initialSize");
        final String minIdleString = props.getValue("db.minIdle");
        Integer maxActive = DEFAULT_MAX_ACTIVE;
        Integer initialSize = DEFAULT_INITIAL_SIZE;
        Integer minIdle = DEFAULT_MIN_IDLE;
        try{
            maxActive = new Integer(maxActiveSrting);
            initialSize = new Integer(initialSizeString);
            minIdle = new Integer(minIdleString);
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            LOG.info("配置文件中的maxActive/initialSize/minIdle格式不正确，出现异常，值会被设置为默认值");
            LOG.info("{}, {}", nfe.getMessage(), nfe.getStackTrace());
        }
        ds.setMaxActive(maxActive);
        ds.setInitialSize(initialSize);
        ds.setMinIdle(minIdle);

        LOG.debug("Druid数据库连接池初始化完成, {}",ds.dump());
        return ds;
    }
}
