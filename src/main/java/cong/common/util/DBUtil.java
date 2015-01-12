/**
 * 2014年2月21日 下午4:37:51
 */
package cong.common.util;

import com.alibaba.druid.pool.DruidDataSource;
import cong.common.dao.SQLCache;
import jodd.bean.BeanUtil;
import jodd.props.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cong 刘聪 onion_sheep@163.com|onionsheep@gmail.com
 */
public class DBUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();


    //暂时把数据源的初始化写在这里，后面要用配置文件进行加载
    private static DruidDataSource dds = null;

    static {
        dds = reInitDds();
    }

    public static DruidDataSource reInitDds() {
        DruidDataSource ds = new DruidDataSource();
        final Props props = Config.load();
        ds.setDriverClassName(props.getValue("db.driver"));
        ds.setUrl(props.getValue("db.url"));
        ds.setUsername(props.getValue("db.username"));
        ds.setPassword(props.getValue("db.password"));
        return ds;
    }


    public static Connection getConnection() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection == null || connection.isClosed()) {
            if (dds == null) {
                dds = reInitDds();
            }
            try {
                connection = dds.getConnection();
            } catch (SQLException e) {
                LOG.error("获取数据库连接异常，异常信息{}", e.getMessage());
                LOG.info("数据源将被重置。");
                dds = reInitDds();
                e.printStackTrace();
            }
            connectionThreadLocal.set(connection);
        }
        return connection;
    }

    public static void closeThreadConnection() {
        final Connection connection = connectionThreadLocal.get();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOG.debug("关闭connection发生异常");
            e.printStackTrace();
        }
    }

    /**
     * 从ResultSet中获取对象列表
     *
     * @param resultSet ResultSet对象
     * @param clazz     要获取的对象的类型
     * @param <T>       泛型类型
     * @return 指定对象的列表
     */
    public static <T> ArrayList<T> getBeanListFromResultSet(ResultSet resultSet,
                                                            Class<T> clazz) {
        ArrayList<T> tList = new ArrayList<T>();

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            SQLCache sqlCache = SQLCache.getSqlCache(clazz);
            ConcurrentHashMap<String, String> columnFieldMap = sqlCache.getColumnFieldMap();

            LinkedList<String> columnNameList = new LinkedList<String>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNameList.add(columnName);
            }

            while (resultSet.next()) {
                T t = clazz.newInstance();
                for (String columnName : columnNameList) {
                    Object o = resultSet.getObject(columnName);
                    String fieldName = columnFieldMap.get(columnName);
                    if (fieldName != null) {
                        BeanUtil.setDeclaredProperty(t, fieldName, o);
                    } else if (LOG.isDebugEnabled()) {
                        LOG.warn("fieldName {} is not found in Class {} and will be ignored."
                                , fieldName, clazz.getName());
                    }
                }
                tList.add(t);
            }
        } catch (SQLException e) {
            LOG.error("SQL异常，{}", e.getMessage());
            e.printStackTrace();
        } catch (InstantiationException e) {
            LOG.error("初始化对象错误，{}", e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            LOG.error("访问权限错误，{}", e.getMessage());
            e.printStackTrace();
        }
        return tList;
    }

    /**
     * 每一行是一个Map，所有Map组成一个List，Map的key是columnLabel
     *
     * @param resultSet 结果集
     * @return MapList结构的结果集
     */
    public static ArrayList<HashMap<String, Object>> getMapListFromResultSet(ResultSet resultSet) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<String> columnLabelList = new ArrayList<String>();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                columnLabelList.add(columnLabel);
            }
            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (String colunmName : columnLabelList) {
                    Object obj = resultSet.getObject(colunmName);
                    map.put(colunmName, obj);
                }
                list.add(map);
            }
        } catch (SQLException e) {
            LOG.error("SQL异常，{}", e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 把ResultSet中的数据作为一个object数组的列表获取出来。每一行是一个Object[]。
     *
     * @param resultSet 结果集
     * @return 每一行是一个Object[]的数组列表
     */
    public static ArrayList<Object[]> getObjectArrayListFromResultSet(ResultSet resultSet) {
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Object[] objctArray = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    objctArray[i] = resultSet.getObject(i + 1);
                }
                list.add(objctArray);
            }
        } catch (SQLException e) {
            LOG.error("SQL异常，{}", e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
