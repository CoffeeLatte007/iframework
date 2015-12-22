package org.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 * 提供模板方法，让子类方便实现
 * Created by lizhaoz on 2015/12/20.
 */

public abstract class AspectProxy implements Proxy {
    private static final Logger LOGGER= LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable{
        Object result=null;
        Class<?> cls=proxyChain.getTargetClass();
        Method method=proxyChain.getTargetMethod();
        Object[] params =proxyChain.getMethodParams();
        begin();//begin的模板
        try{
            //拦截的模板
            if(intercept(cls,method,params)){
                //前置增强的模板
                before(cls,method,params);
                result =proxyChain.doProxyChain();
                after(cls,method,params,result);
            }
            else {
                result = proxyChain.doProxyChain();
            }
        }catch (Exception e){
            LOGGER.error("proxy failure",e);
            error(cls,method,params,e);
            throw e;
        }
        finally {
            end();
        }
        return result;
    }

    public void end() throws Throwable{
    }

    public void error(Class<?> cls, Method method, Object[] params, Exception e)throws Throwable {

    }

    public void after(Class<?> cls, Method method, Object[] params, Object result)throws Throwable {

    }

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable{

    }

    public boolean intercept(Class<?> cls, Method method, Object[] params)throws Throwable {
        return true;
    }

    public void begin() throws Throwable{
    }
}
