package common.orm.core;

/**
 * 负责java数据类型和数据库数据类型的互相转换
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public interface TypeConvertor {

    /**
     * 将数据库数据类型转化成java的数据类型
     *
     * @param columnType 数据库字段的数据类型
     * @return java的数据类型
     */
    public String databaseTypeToJavaType(String columnType);

    /**
     * 将java数据类型转化成数据库数据类型
     *
     * @param javaDataType java数据类型
     * @return 数据库类型
     */
    public String javaTypeToDatabaseType(String javaDataType);

}
