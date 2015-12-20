package org.framework.proxy;

/**
 * 代理接口
 * Created by lizhaoz on 2015/12/20.
 */
public interface Proxy {

    /**
     * 执行链式代理
     * author：Lizhao
     * Date:15/12/20
     * version:1.0
     *
     * @param proxyChain
     *
     * @return
     *
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}


//~ Formatted by Jindent --- http://www.jindent.com
