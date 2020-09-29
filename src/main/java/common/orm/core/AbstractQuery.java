package common.orm.core;


import common.orm.bean.ColumnInfo;
import common.orm.bean.TableContext;
import common.orm.bean.TableInfo;
import common.orm.util.JDBCUtil;
import common.orm.util.ReflectUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询（对外提供服务的核心类）
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title AbstractQuery
 * @date 2020/5/24 14:54
 * @modified by:
 */
public abstract class AbstractQuery implements Cloneable {

    /**
     * 采用模板方法模式将JDBC操作封装成模板，便于重用
     *
     * @param sql    sql语句
     * @param params sql的参数
     * @param clazz  记录要封装到的java类
     * @param back   CallBack的实现类，实现回调
     * @return 查询的对象
     */
    public Object executeQueryTemplate(String sql, Object[] params, Class<?> clazz, CallBack back) {
        Connection connection = DatabaseManager.getConn();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            //给sql设置参数，完善sql语句
            JDBCUtil.handleParams(preparedStatement, params);
            resultSet = preparedStatement.executeQuery();
            return back.doExecute(connection, preparedStatement, resultSet);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DatabaseManager.close(preparedStatement, connection);
        }
    }


    /**
     * 直接执行一个DML语句
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 执行sql语句后影响记录的行数
     */
    public int executeDML(String sql, Object[] params) {
        Connection conn = DatabaseManager.getConn();
        int count = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            //给sql设参
            JDBCUtil.handleParams(preparedStatement, params);

            count = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.close(preparedStatement, conn);
        }

        return count;
    }

    /**
     * 将一个对象存储到数据库中
     * 把对象中不为null的属性往数据库中存储！如果数字为null则放0.
     *
     * @param obj 要存储的对象
     */
    public void insert(Object obj) {
        Class<?> c = obj.getClass();
        //存储sql的参数对象
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);

        StringBuilder sql = new StringBuilder("insert into `" + tableInfo.getTableName() + "` (");

        //计算不为null的属性值
        int countNotNullField = 0;
        Field[] fs = c.getDeclaredFields();
        for (Field f : fs) {
            String fieldName = f.getName();
            Object fieldValue = ReflectUtil.invokeGet(fieldName, obj);

            if (fieldValue != null) {
                countNotNullField++;
                sql.append(fieldName).append(",");
                params.add(fieldValue);
            }
        }

        sql.setCharAt(sql.length() - 1, ')');
        sql.append(" values (");
        for (int i = 0; i < countNotNullField; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length() - 1, ')');

        executeDML(sql.toString(), params.toArray());

    }

    /**
     * 删除clazz表示类对应的表中的记录(指定主键值id的记录)
     *
     * @param clazz 跟表对应的类的Class对象
     * @param id    主键的值
     */
    public void delete(Class<?> clazz, Object id) {
        //通过Class对象找TableInfo
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        //获得主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        String sql = "delete from " + tableInfo.getTableName() + " where " + onlyPriKey.getFiledName() + "=? ";

        executeDML(sql, new Object[]{id});

    }

    /**
     * 执行特定sql删除语句
     *
     * @param sql
     * @param params
     */
    public void delete(String sql, Object[] params) {
        executeDML(sql, params);
    }

    /**
     * 删除对象在数据库中对应的记录(对象所在的类对应到表，对象的主键的值对应到记录)
     *
     * @param obj 删除对象
     */
    public void delete(Object obj) {
        Class<?> c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        //主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        //通过反射机制，调用属性对应的get方法或set方法
        Object priKeyValue = ReflectUtil.invokeGet(onlyPriKey.getFiledName(), obj);

        delete(c, priKeyValue);

    }

    /**
     * 更新对象对应的记录，并且只更新指定的字段的值
     *
     * @param obj        所要更新的对象
     * @param fieldNames 更新的属性列表
     * @return 执行sql语句后影响记录的行数
     */
    public int update(Object obj, String[] fieldNames) {
        Class<?> c = obj.getClass();
        //存储sql的参数对象
        List<Object> params = new ArrayList<Object>();

        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        //获得唯一的主键
        ColumnInfo priKey = tableInfo.getOnlyPriKey();

        StringBuilder sql = new StringBuilder("update " + tableInfo.getTableName() + " set ");

        for (String fieldName : fieldNames) {
            Object fieldValue = ReflectUtil.invokeGet(fieldName, obj);
            params.add(fieldValue);
            sql.append(fieldName).append("=?,");
        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append(" where ");
        sql.append(priKey.getFiledName()).append("=? ");

        //主键的值
        params.add(ReflectUtil.invokeGet(priKey.getFiledName(), obj));

        return executeDML(sql.toString(), params.toArray());

    }

    /**
     * 查询返回多行记录，并将每行记录封装到clazz指定的类的对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的javabean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public List<?> queryRows(final String sql, final Class<?> clazz, final Object[] params) {

        return (List<?>) executeQueryTemplate(sql, params, clazz, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                List list = null;
                try {
                    ResultSetMetaData metaData = rs.getMetaData();
                    //多行
                    while (rs.next()) {
                        if (list == null) {
                            list = new ArrayList<>();
                        }

                        //调用javabean的无参构造器
                        Object rowObj = clazz.newInstance();

                        //多列
                        for (int i = 0; i < metaData.getColumnCount(); i++) {
                            String columnName = metaData.getColumnLabel(i + 1);
                            Object columnValue = rs.getObject(i + 1);

                            //调用rowObj对象的set方法，将columnValue的值设置进去
                            ReflectUtil.invokeSet(rowObj, columnName, columnValue);
                        }

                        list.add(rowObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return list;
            }
        });


    }


    /**
     * 查询返回一行记录，并将该记录封装到clazz指定的类的对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的javabean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
        List list = queryRows(sql, clazz, params);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    /**
     * 根据主键的值直接查找对应的对象
     *
     * @param clazz 查找对象的类
     * @param id    主键值
     * @return 找到的对象
     */
    public Object queryById(Class clazz, Object id) {
        //select * from emp where id=?
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        //获得主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        String sql = "select * from " + tableInfo.getTableName() + " where " + onlyPriKey.getFiledName() + "=? ";
        return queryUniqueRow(sql, clazz, new Object[]{id});
    }


    /**
     * 查询返回一个值(一行一列)，并将该值返回
     *
     * @param sql    查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryValue(String sql, Object[] params) {
        return executeQueryTemplate(sql, params, null, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                Object value = null;
                try {
                    while (rs.next()) {
                        value = rs.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });
    }

    /**
     * 查询返回一个数字(一行一列)，并将该值返回
     *
     * @param sql    查询语句
     * @param params sql的参数
     * @return 查询到的数字
     */
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql, params);
    }

    /**
     * 分页查询
     *
     * @param pageNum 第几页数据
     * @param size    每页显示多少记录
     * @return 查询的数据对象
     */
    public abstract Object queryPaginate(int pageNum, int size);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
