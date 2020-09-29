package common.orm.bean;

/**
 * 封装表中一个字段的信息
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class ColumnInfo {
    /**
     * 字段名称
     */
    private String filedName;

    /**
     * 字段的数据类型
     */
    private String dataType;

    /**
     * 字段的键类型(0：普通键，1：主键 2：外键)
     */
    private int keyType;

    public ColumnInfo(String filedName, String dataType, int keyType) {
        super();
        this.filedName = filedName;
        this.dataType = dataType;
        this.keyType = keyType;
    }


    public ColumnInfo() {
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }
}
