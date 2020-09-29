package common.orm.bean;

import common.orm.core.DatabaseManager;
import common.orm.core.MySqlTypeConvertor;
import common.orm.util.JavaFileUtil;
import common.orm.util.StringUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构。
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class TableContext {

    /**
     * 表名为key，表信息对象为value
     */
    public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

    /**
     * 将po的class对象和表信息对象关联起来，便于重用！
     */
    public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

    private TableContext() {
    }

    static {
        try {
            //初始化获得表的信息
            Connection connection = DatabaseManager.getConn();
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            String url = DatabaseManager.getConf().getUrl();
            String databaseName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));

            ResultSet tableRet = databaseMetaData.getTables(databaseName, "%", "%",
                    new String[]{"TABLE"});

            while (tableRet.next()) {
                String tableName = (String) tableRet.getObject("TABLE_NAME");

                TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
                tables.put(tableName, ti);

                //查询表中的所有字段
                ResultSet set = databaseMetaData.getColumns(null, "%", tableName,
                        "%");
                while (set.next()) {
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"), 0);
                    ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
                }

                //查询t_user表中的主键
                ResultSet set2 = databaseMetaData.getPrimaryKeys(null, "%", tableName);
                while (set2.next()) {
                    ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
                    //设置为主键类型
                    ci2.setKeyType(1);
                    ti.getPriKeys().add(ci2);
                }

                //取唯一主键。。方便使用。如果是联合主键。则为空！
                if (ti.getPriKeys().size() > 0) {
                    ti.setOnlyPriKey(ti.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //更新类结构
        // TODO 暂时不更新 updateJavaPOFile();


    }

    /**
     * 根据表结构，更新配置的po包下面的java类
     * 实现了从表结构转化到类结构
     */
    public static void updateJavaPOFile() {
        Map<String, TableInfo> map = TableContext.tables;
        for (TableInfo t : map.values()) {
            JavaFileUtil.createJavaPOFile(t, new MySqlTypeConvertor());
        }
    }

    /**
     * 加载po包下面的类
     */
    public static void loadPOTables() {

        for (TableInfo tableInfo : tables.values()) {
            try {
                Class c = Class.forName(DatabaseManager.getConf().getPoPackage()
                        + "." + StringUtil.firstCharToUpper(tableInfo.getTableName()));
                poClassTableMap.put(c, tableInfo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}
