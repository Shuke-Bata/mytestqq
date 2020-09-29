package common.orm.core;


import common.orm.bean.TableContext;

/**
 * 创建Query对象的工厂类
 *
 * @author Minghua Zhou 周明华
 * @version 1.0
 * @title AbstractQuery
 * @date 2020/5/24 14:54
 * @modified by:
 */
public class QueryFactory {
    /**
     * 原型对象
     */
    private static AbstractQuery prototypeObj;

    static {

        //加载指定的query类
        try {
            Class c = Class.forName(DatabaseManager.getConf().getQueryClass());
            prototypeObj = (AbstractQuery) c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //加载po包下面所有的类，便于重用，提高效率！
        TableContext.loadPOTables();
    }

    private QueryFactory() {
    }


    /**
     * 创建查询类
     *
     * @return AbstractQuery
     */
    public static AbstractQuery createQuery() {
        try {
            return (AbstractQuery) prototypeObj.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
