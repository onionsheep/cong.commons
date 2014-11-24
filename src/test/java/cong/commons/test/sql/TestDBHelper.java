/**
 * 创建 @ 2013年7月16日 下午3:58:40
 * 
 */
package cong.commons.test.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

import cong.commons.sql.DBHelper;

/**
 * @author 刘聪 (onion_sheep@163.com | onionsheep@gmail.com)
 * 
 */
public class TestDBHelper {
    private static final Logger    log = LoggerFactory.getLogger(TestDBHelper.class);

    private static DruidDataSource dataSource;
    private static DBHelper        dbHelper;

    @BeforeClass
    public static void init() {

        // String jdbcUrl = "jdbc:h2:tcp://localhost/~/test";
        // String user = "sa";
        // String password = "";
        // String driverClass = "org.h2.Driver";

        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/congtest";
        String user = "root";
        String password = "";
        String driverClass = "com.mysql.jdbc.Driver";

        dataSource = new DruidDataSource();

        // dataSource.setInitialSize(initialSize);
        // dataSource.setMaxActive(maxActive);
        // dataSource.setMinIdle(minIdle);
        // dataSource.setMaxIdle(maxIdle);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(jdbcUrl);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        // dataSource.setValidationQuery(validationQuery);
        // dataSource.setTestOnBorrow(testOnBorrow);
        // dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dbHelper = new DBHelper(dataSource);
    }

    /**
     * Test method for
     * {@link cong.commons.sql.DBHelper#getEntityListFromResultSet(java.lang.Class, java.sql.ResultSet)}
     * .
     * 
     * @throws ClassNotFoundException
     */
    // @Test
    @Ignore
    public void testGetList() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        String query = "select * from t_person";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<Person> list = dbHelper.getEntityListFromResultSet(Person.class, rs);
        for (Person p : list) {
            System.out.println(p.toString());
        }
        conn.close();

    }

    @Test
    @Ignore
    public void testTimeType() {
        Entity1 e10 = new Entity1();
        e10.setT(new Date());
        dbHelper.preperedInsert(e10);
        dbHelper.insert(e10);

        Entity1 e1get = dbHelper.getEntityById(Entity1.class, 14);
        log.info("{}", e1get);
    }

    // @Test
    @Ignore
    public void testGetEntityByIdNamePK() {

        Entity0 e0 = dbHelper.getEntityById(Entity0.class, 1);
        log.info(e0.toString());

        Entity0 e1 = dbHelper.getEntityByName(Entity0.class, "n");
        log.info(e1.toString());

        Entity0 e2 = dbHelper.getEntityByPK(e1);
        log.info(e2.toString());

        e0.setString("asdf");
        int er0 = dbHelper.insert(e0);
        log.info("insert effetced rows : {}", er0);

        e1.setInteger(123);
        int er1 = dbHelper.update(e1);
        log.info("update effetced rows : {}", er1);

        e0.setId(9);
        int er3 = dbHelper.delete(e0);
        log.info("delete effetced rows : {}", er3);

    }

    // @Test
    @Ignore
    public void testPrepared() {

        Entity0 e0 = new Entity0();
        e0.setString("prepared insert");
        int er4 = dbHelper.preperedInsert(e0);
        log.info("prepared insert effetced rows : {}", er4);

        Entity0 e1 = new Entity0();
        e1.setInteger(345);
        e1.setId(5);
        int er5 = dbHelper.preparedUpdateByKey(e1);
        log.info("prepared update effetced rows : {}", er5);

        e0.setId(11);
        int er6 = dbHelper.preparedDeleteByKey(e0);
        log.info("prepared delete effetced rows : {}", er6);

        Entity0 e3 = new Entity0();
        e3.setId(23);
        e3 = dbHelper.preperedSelectByKey(e3);
        log.info("prepare select result: {}", e3);
    }

    // @Test
    @Ignore
    public void testTime() {
        Entity0 e0 = new Entity0();
        // e0.setString("pstmt");
        e0.setId(10000);
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            // e0.setInteger(i);
            dbHelper.preperedSelectByKey(e0);
        }
        long t1 = System.currentTimeMillis();

        log.info("pstmt cost time: {}", t1 - t0);

        // e0.setString("stmt");
        e0.setId(10000);
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            // e0.setInteger(i);
            dbHelper.getEntityById(Entity0.class, 10000);
        }
        long t3 = System.currentTimeMillis();

        log.info("stmt  cost time: {}", t3 - t2);

    }

}
