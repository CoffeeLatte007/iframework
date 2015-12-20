package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.util.ReflectionUtil;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean助手类
 * Created by lizhaoz on 2015/12/16.
 */
public class BeanHelper {

    // 存放Bean类和Bean实例的映射
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();

        for (Class<?> beanClass : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);

            BEAN_MAP.put(beanClass, obj);
        }
    }

    /**
     * 获取Bean映射
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 得到该Bean的实例
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param cls
     * @param <T>
     *
     * @return
     */
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class: " + cls);
        }

        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置Bean实例，一般来说 对于代理对象的实例 设置就用这个方法
     * author：Lizhao
     * Date:15/12/20
     * version:1.0
     *
     * @param cls
     * @param obj
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
