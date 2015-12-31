package org.framework.proxy;

import org.framework.annotation.Transaction;
import org.framework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事务代理
 * Created by lizhaoz on 2015/12/21.
 */

public class TransactionProxy implements Proxy {
    private static final Logger LOGGER= LoggerFactory.getLogger(TransactionProxy.class);
    private static final ThreadLocal<Boolean> FLAG_HOLDER=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag=FLAG_HOLDER.get();
        Method method=proxyChain.getTargetMethod();
        //
        if (!flag&&method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try{
                DatabaseHelper.beginTranstion();
                LOGGER.debug("begin transaction");
                result =proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                DatabaseHelper.rollbackTransaction();
                LOGGER.debug("rollback transaction");
                throw e;
            }
            finally {
                FLAG_HOLDER.remove();
            }
        }
        else {
            result=proxyChain.doProxyChain();
        }
        return result;
    }
}
