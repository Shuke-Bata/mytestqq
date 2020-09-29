package common.orm.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了JDBC查询常用的操作
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class JDBCUtil {

    /**
     * 给sql设置参数的值
     *
     * @param preparedStatement 预编译sql语句对象
     * @param params            参数
     */
    public static void handleParams(PreparedStatement preparedStatement, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    preparedStatement.setObject(1 + i, params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
