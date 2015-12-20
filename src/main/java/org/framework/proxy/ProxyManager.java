package org.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器
 * 我们使用CGLIB提供的ENhancer#create方法来创建代理对象，将intercept的参数传入ProxyChain的构造器中即可
 * Created by lizhaoz on 2015/12/20.
 */

public class ProxyManager {
    public static <T> T createProxy(final Class<?> targetClass,final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method  targetmethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObject,targetmethod,methodProxy,methodParams,proxyList).doProxyChain();
            }
        });
    }
}
