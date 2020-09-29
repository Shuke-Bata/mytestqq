package common.orm.util;

/**
 * 封装了字符串常用的操作
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title StringUtil
 * @date 2020/5/24 15:43
 * @modified by:
 */
public class StringUtil {
    /**
     * 首字母转大写
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String firstCharToUpper(String str) {
        return str.toUpperCase().substring(0, 1) + str.substring(1);
    }

    /**
     * 首字母转小写
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String firstCharToLower(String str) {
        return str.toLowerCase().substring(0, 1) + str.substring(1);
    }

}
