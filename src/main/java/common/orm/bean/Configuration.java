package common.orm.bean;

/**
 * 管理配置信息
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title Configuration
 * @date 2020/5/24 14:56
 * @modified by:
 */
public class Configuration {
    /**
     * 驱动类
     */
    private String driver;
    /**
     * jdbc的url
     */
    private String url;
    /**
     * 数据库的用户名
     */
    private String userName;
    /**
     * 数据库的密码
     */
    private String password;
    /**
     * 扫描生成java类的包(po的意思是：Persistence object持久化对象)
     */
    private String poPackage;
    /**
     * 项目使用的查询类是哪一个类
     */
    private String queryClass;
    /**
     * 连接池中最小的连接数
     */
    private int poolMinSize;
    /**
     * 连接池中最大的连接数
     */
    private int poolMaxSize;


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }

    public int getPoolMinSize() {
        return poolMinSize;
    }

    public void setPoolMinSize(int poolMinSize) {
        this.poolMinSize = poolMinSize;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }
}
