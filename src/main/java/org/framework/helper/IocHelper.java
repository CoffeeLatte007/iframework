package org.framework.helper;


import org.framework.annotation.Inject;
import org.framework.util.ArrayUtil;
import org.framework.util.CollectionUtil;
import org.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类 用于Inject
 * Created by lizhaoz on 2015/12/16.
 */

public final class IocHelper {
    static {
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
                if(CollectionUtil.isNotEmpty(beanMap)){
                    //遍历BeanMap
                    for(Map.Entry<Class<?>,Object> beanEntry :beanMap.entrySet()){
                        //从BeanMap得到Bean类和Bean实例
                        Class<?> beanClass =beanEntry.getKey();
                Object beanInstance=beanEntry.getValue();
                //获取Bean类定义的所有成员变量
                Field[] beanFields=beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    //遍历成员变量
                    for (Field beanField:beanFields){
                        //判断当前Bean Field是否带有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            //得到Bean Field的实例类
                            Class<?> beanFieldClass=beanField.getType();
                            Object beanFieldInstance=beanMap.get(beanFieldClass);
                            if(beanFieldInstance !=null){
                                //通过反射初始化值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
