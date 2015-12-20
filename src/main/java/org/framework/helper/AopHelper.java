package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.spi.LoggerFactory;
import org.framework.annotation.Aspect;
import org.framework.proxy.AspectProxy;
import org.framework.proxy.Proxy;
import org.framework.proxy.ProxyManager;
import org.slf4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;

import java.util.*;

/**
 * Created by lizhaoz on 2015/12/20.
 */
public class AopHelper {
    private static final Logger LOGGER= org.slf4j.LoggerFactory.getLogger(AopHelper.class);
    static {
        try{
            Map<Class<?>,Set<Class<?>>> proxyMap=createProxyMap();
            Map<Class<?>,List<Proxy>> targetMap=createTargetMap(proxyMap);
            for(Map.Entry<Class<?>,List<Proxy>> targetEntry:targetMap.entrySet()){
                Class<?> targetClass =targetEntry.getKey();
                List<Proxy> proxyList=targetEntry.getValue();
                Object proxy= ProxyManager.createProxy(targetClass,proxyList);
                //添加目标类和代理实例
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop failure",e);
        }
    }

    /**
     * 获得目标类的方法
     * author：Lizhao
     * Date:15/12/20
     * version:1.0
     *
     * @param aspect
     *
     * @return 目标类的set集合
     *
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();

        // 获取注解的类
        Class<? extends Annotation> annotation = aspect.value();

        // 不为空，或者不是Aspect的注解类都行
        if ((annotation != null) &&!annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }

        return targetClassSet;
    }

    /**
     * 代理类和目标类的映射关系
     * author：Lizhao
     * Date:15/12/20
     * version:1.0
     *
     * @return
     *
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {

        // 代理类和映射类的关系 Map
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();

        // 获得所有代理类，只要继承了AspectProxy 均为代理类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);

        // 遍历代理类
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {

                // 得到aspect
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);

                // 通过aspect 例如Controller.class的值，获得所有的目标类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);

                proxyMap.put(proxyClass, targetClassSet);
            }
        }

        return proxyMap;
    }

    /**
     *目标类和代理对象列表之间的映射关系
     * author：Lizhao
     * Date:15/12/20
     * version:1.0
     *
     * @param proxyMap
     *
     * @return
     *
     * @throws Exception
     */
    public static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        //目标类和代理列表实例映射map
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        //遍历代理类和目标类的映射Map
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            //代理类
            Class<?>      proxyClass     = proxyEntry.getKey();
            //目标集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            //遍历目标类集合，每个都加入新的目标map
            for (Class<?> targetClass : targetClassSet) {
                //实例化代理类
                Proxy proxy = (Proxy) proxyClass.newInstance();

                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();

                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }

        return targetMap;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
