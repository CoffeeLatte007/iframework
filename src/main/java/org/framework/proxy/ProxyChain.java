package org.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoz on 2015/12/20.
 */

public class ProxyChain {
    private final Class<?> targetClass; //目标类
    private final Object targetObject;//目标对象
    private final Method targetMethod;//目标方法
    private final MethodProxy methodProxy;//方法代理
    private final Object[] methodParams;//方法参数
    private List<Proxy> proxyList=new ArrayList<Proxy>(); //代理列表
    private int proxyIndex = 0;//代理索引

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }
    public Object[] getMethodParams(){
        return methodParams;
    }
    public Class<?> getTargetClass(){
        return targetClass;
    }
    public Method getTargetMethod(){
        return targetMethod;
    }
    public Object doProxyChain() throws Throwable{
        Object methodResult;
        if(proxyIndex<proxyList.size()){
            //代理策略，通过proxyIndex来充当代理对象的计数器，若未达到proxyList的上限
            //就执行proxy对象的proxy方法
            //最后就执行目标对象的业务逻辑方法
            methodResult=proxyList.get(proxyIndex++).doProxy(this);
        }else {
            //执行业务逻辑方法
            methodResult=methodProxy.invokeSuper(targetObject,methodParams);
        }
        return  methodResult;
    }
}
