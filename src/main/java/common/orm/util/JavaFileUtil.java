package common.orm.util;


import common.orm.bean.ColumnInfo;
import common.orm.bean.JavaFieldGetSet;
import common.orm.bean.TableInfo;
import common.orm.core.DatabaseManager;
import common.orm.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了生成Java文件(源代码)常用的操作
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class JavaFileUtil {

    /**
     * 根据字段信息生成java属性信息。如：varchar username-->private String username;以及相应的set和get方法源码
     *
     * @param column    字段信息
     * @param convertor 类型转化器
     * @return java属性和set/get方法源码
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor) {
        JavaFieldGetSet jfgs = new JavaFieldGetSet();

        String javaFieldType = convertor.databaseTypeToJavaType(column.getDataType());

        jfgs.setFieldInfo("\tprivate " + javaFieldType + " " + column.getFiledName() + ";\n");

        //public String getUsername(){return username;}
        //生成get方法的源代码
        StringBuilder getSrc = new StringBuilder();
        getSrc.append("\tpublic " + javaFieldType + " get" + StringUtil.firstCharToUpper(column.getFiledName()) + "(){\n");
        getSrc.append("\t\treturn " + column.getFiledName() + ";\n");
        getSrc.append("\t}\n");
        jfgs.setGetInfo(getSrc.toString());

        //public void setUsername(String username){this.username=username;}
        //生成set方法的源代码
        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic void set" + StringUtil.firstCharToUpper(column.getFiledName()) + "(");
        setSrc.append(javaFieldType + " " + column.getFiledName() + "){\n");
        setSrc.append("\t\tthis." + column.getFiledName() + "=" + column.getFiledName() + ";\n");
        setSrc.append("\t}\n");
        jfgs.setSetInfo(setSrc.toString());
        return jfgs;
    }

    /**
     * 根据表信息生成java类的源代码
     *
     * @param tableInfo 表信息
     * @param convertor 数据类型转化器
     * @return java类的源代码
     */
    public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor) {

        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();

        for (ColumnInfo c : columns.values()) {
            javaFields.add(createFieldGetSetSRC(c, convertor));
        }

        StringBuilder src = new StringBuilder();

        //生成package语句
        src.append("package ").append(DatabaseManager.getConf().getPoPackage()).append(";\n\n");
        src.append("import java.io.Serializable;\n\n");
        //生成类声明语句
        src.append("public class ").append(StringUtil.firstCharToUpper(tableInfo.getTableName())).
                append(" implements Serializable {\n\n");

        //生成属性列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getFieldInfo());
        }
        src.append("\n\n");
        //生成get方法列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getGetInfo());
        }
        //生成set方法列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getSetInfo());
        }

        //生成类结束
        src.append("}\n");
        return src.toString();
    }


    public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor convertor) {
        String src = createJavaSrc(tableInfo, convertor);

        String srcPath = System.getProperty("user.dir") + "\\src\\main\\java\\";
        String packagePath = DatabaseManager.getConf().getPoPackage().replaceAll("\\.", "/");

        File f = new File(srcPath + packagePath);

        //如果指定目录不存在，则帮助用户建立
        if (!f.exists()) {
            f.mkdirs();
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile() + "/" + StringUtil.firstCharToUpper(tableInfo.getTableName()) + ".java"));
            bw.write(src);
            System.out.println("建立表" + tableInfo.getTableName() +
                    "对应的java类：" + StringUtil.firstCharToUpper(tableInfo.getTableName()) + ".java");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
