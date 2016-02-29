package org.framework.util;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * Created by lizhaoz on 2015/12/16.
 */
public final class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建类得实例，调用默认构造函数
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param cls
     *
     * @return
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;

        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance failure", e);

            throw new RuntimeException(e);
        }

        return instance;
    }

    /**
     * 反射调用方法
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param obj
     * @param method
     * @param args
     *
     * @return
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;

        method.setAccessible(true);    // 提高反射速度，抑制了对修饰符的检查

        try {
            result = method.invoke(obj, args);    // 反射执行方法
        } catch (Exception e) {
            LOGGER.error("invoke method failure", e);

            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 给某个域反射赋值
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);

        try {
            field.set(obj, value);
        } catch (Exception e) {
            LOGGER.error("set field failure", e);

            throw new RuntimeException(e);
        }
    }

    public static Object newInstance(String className) {
        Class<?> cls=ClassUtil.loadClass(className);
        return newInstance(cls);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
