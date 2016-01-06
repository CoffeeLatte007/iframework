package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet助手类 为每一个线程提供独立的response和request
 * Created by lizhaoz on 2016/1/6.
 */
public final class ServletHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletHelper.class);

    // 每个线程都独自拥有一份ServletHelper实例
    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<ServletHelper>();
    private HttpServletRequest                      request;
    private HttpServletResponse                     response;

    private ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request  = request;
        this.response = response;
    }

    /**
     * 初始化 ServletHelper 注入request和response
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param request
     * @param response
     */
    public static void init(HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER_HOLDER.set(new ServletHelper(request, response));
    }

    /**
     * 销毁
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     */
    public static void destory() {
        SERVLET_HELPER_HOLDER.remove();
    }

    /**
     * 获取Request对象
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return SERVLET_HELPER_HOLDER.get().request;
    }

    /**
     * 获取Response对象
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @return
     */
    private static HttpServletResponse getResponse() {
        return SERVLET_HELPER_HOLDER.get().response;
    }

    /**
     * 获取session
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @return
     */
    private static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取ServletContext
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @return
     */
    private static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    /**
     * 向request中添加属性
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param key
     * @param value
     */
    public static void setRequestAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }

    /**
     * 从request中得到属性
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param key
     * @param <T>
     *
     * @return
     */
    public static <T> T getRequestAttribute(String key) {
        return (T) getRequest().getAttribute(key);
    }

    /**
     * 从request中移除属性
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param key
     */
    public static void remoteRequestAttribute(String key) {
        getRequest().removeAttribute(key);
    }

    /**
     * 发送重定向
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param location
     */
    public static void sendRedirect(String location) {
        try {
            getResponse().sendRedirect(getRequest().getContextPath() + location);
        } catch (IOException e) {
            LOGGER.error("redirect failure", e);
        }
    }

    /**
     * 向Session中添加属性
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param key
     * @param value
     */
    public static void setSessionAttribute(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 从Session中获取属性
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param key
     * @param <T>
     *
     * @return
     */
    public static <T> T getSessionAttribute(String key) {
        return (T) getRequest().getSession().getAttribute(key);
    }

    /**
     * 从Session中移除属性
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param key
     */
    public static void removeSessionAttribute(String key) {
        getRequest().getSession().removeAttribute(key);
    }

    /**
     * 让Session失效
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     */
    public static void invalidateSession() {
        getRequest().getSession().invalidate();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
