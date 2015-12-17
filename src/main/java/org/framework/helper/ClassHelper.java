package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.annotation.Controller;
import org.framework.annotation.Service;
import org.framework.util.ClassUtil;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 * Created by lizhaoz on 2015/12/16.
 */
public final class ClassHelper {

    // 存放所有加载的类
    private static final Set<Class<?>> CLASS_SET;

    static {
        //静态块中初始化 CLASS_SET
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 得到有Service注解的类
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }

    /**
     * 得到有Controller注解的类
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }

    /**
     * 得到所有Bean类 包括Service 和Controller
     * author：Lizhao
     * Date:15/12/16
     * version:1.0
     *
     * @return
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();

        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());

        return beanClassSet;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
