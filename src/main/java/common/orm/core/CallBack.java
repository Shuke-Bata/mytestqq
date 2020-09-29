package common.orm.core;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 回调接口
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title CallBack
 * @date 2020/5/24 14:54
 * @modified by:
 */
public interface CallBack {
    /**
     * 执行回调,
     *
     * @param conn 连接
     * @param ps   PreparedStatement
     * @param rs   rs
     * @return Object
     */
    public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs);
}
