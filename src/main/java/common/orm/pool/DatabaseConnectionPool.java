package common.orm.pool;


import common.orm.core.DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库连接池(单例设计模式)
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseConnectionPool
 * @date 2020/5/24 14:50
 * @modified by:
 */
public class DatabaseConnectionPool {

    public static DatabaseConnectionPool databaseConnectionPool;
    /**
     * 连接池对象
     */
    private List<Connection> pool;
    /**
     * 最大连接数
     */
    private static final int POOL_MAX_SIZE = DatabaseManager.getConf().getPoolMaxSize();
    /**
     * 最小连接池
     */
    private static final int POOL_MIN_SIZE = DatabaseManager.getConf().getPoolMinSize();

    private DatabaseConnectionPool() {
        initPool();
    }

    /**
     * 初始化连接池，使池中的连接数达到最小值
     */
    private void initPool() {
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }

        while (pool.size() < POOL_MIN_SIZE) {
            pool.add(createConn());
            System.out.println("初始化池，池中连接数：" + pool.size());
        }
    }

    /**
     * 创建新的 Connection 对象
     *
     * @return Connection
     */
    private Connection createConn() {
        try {
            Class.forName(DatabaseManager.getConf().getDriver());
            return DriverManager.getConnection(DatabaseManager.getConf().getUrl(),
                    DatabaseManager.getConf().getUserName(), DatabaseManager.getConf().getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从连接池中取出一个连接
     *
     * @return Connection
     */
    public synchronized Connection getConnection() {
        int lastIndex = pool.size() - 1;
        Connection conn = pool.get(lastIndex);
        pool.remove(lastIndex);
        return conn;
    }

    /**
     * 将连接放回池中
     *
     * @param conn 连接
     */
    public synchronized void close(Connection conn) {
        if (pool.size() >= POOL_MAX_SIZE) {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            pool.add(conn);
        }
    }

    /**
     * 获取连接池实例，加入DCL双检查锁机制，效率高，线程安全，多线程操作原子性。
     *
     * @return 连接池实例
     */
    public static DatabaseConnectionPool getInstance() {
        if (databaseConnectionPool == null) {
            synchronized (DatabaseConnectionPool.class) {
                if (databaseConnectionPool == null) {
                    databaseConnectionPool = new DatabaseConnectionPool();
                }
            }
        }
        return databaseConnectionPool;
    }
}
