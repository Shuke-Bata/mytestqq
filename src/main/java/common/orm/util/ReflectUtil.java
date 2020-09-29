package common.orm.util;

import java.lang.reflect.Method;

/**
 * 封装了反射常用的操作
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title DatabaseManager
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class ReflectUtil {

    /**
     * 调用obj对象对应属性fieldName的get方法
     *
     * @param fieldName 属性名
     * @param obj       调用对象
     * @return
     */
    public static Object invokeGet(String fieldName, Object obj) {
        try {
            Class<?> c = obj.getClass();
            Method m = c.getDeclaredMethod("get" + StringUtil.firstCharToUpper(fieldName), null);
            return m.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 调用obj对象对应属性fieldName的 set方法
     *
     * @param obj         对象
     * @param columnName  属性名
     * @param columnValue 属性值
     */
    public static void invokeSet(Object obj, String columnName, Object columnValue) {
        try {
            if (columnValue != null) {
                Method m = obj.getClass().getDeclaredMethod("set" + StringUtil.firstCharToUpper(columnName),
                        columnValue.getClass());
                m.invoke(obj, columnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
