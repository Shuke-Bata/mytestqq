package common.orm.core;

import common.orm.bean.Configuration;
import common.orm.pool.DatabaseConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理(增加连接池功能)
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class DatabaseManager {
    /**
     * 配置信息
     */
    private static Configuration conf;
    /**
     * 连接池对象
     */
    private static DatabaseConnectionPool pool;


    private DatabaseManager() {

    }

    private static void init() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        conf = new Configuration();
        conf.setDriver(properties.getProperty("driver"));
        conf.setUrl(properties.getProperty("url"));
        conf.setUserName(properties.getProperty("userName"));
        conf.setPassword(properties.getProperty("password"));
        conf.setPoPackage(properties.getProperty("poPackage"));
        conf.setQueryClass(properties.getProperty("queryClass"));
        conf.setPoolMaxSize(Integer.parseInt(properties.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(properties.getProperty("poolMinSize")));

        pool = DatabaseConnectionPool.getInstance();
    }

    /**
     * 获得Connection对象
     *
     * @return Connection
     */
    public static Connection getConn() {
        if (pool == null) {
            init();
        }
        return pool.getConnection();
    }

    /**
     * 返回Configuration对象
     *
     * @return Configuration
     */
    public static Configuration getConf() {
        if (conf == null) {
            init();
        }
        return conf;
    }

    /**
     * 关闭传入的 ResultSet、Statement、Connection对象
     *
     * @param rs   ResultSet
     * @param ps   Statement
     * @param conn Connection对象
     */
    public static void close(ResultSet rs, Statement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pool.close(conn);

    }

    /**
     * 关闭传入的 Statement、Connection对象
     *
     * @param ps   Statement
     * @param conn Connection对象
     */
    public static void close(Statement ps, Connection conn) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pool.close(conn);
    }

    /**
     * 关闭连接
     *
     * @param conn Connection
     */
    public static void close(Connection conn) {
        pool.close(conn);
    }

}
