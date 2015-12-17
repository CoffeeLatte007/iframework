package org.framework;

//~--- non-JDK imports --------------------------------------------------------

import org.framework.bean.Data;
import org.framework.bean.Handler;
import org.framework.bean.Param;
import org.framework.bean.View;
import org.framework.helper.BeanHelper;
import org.framework.helper.ConfigHelper;
import org.framework.helper.ControllerHelper;
import org.framework.util.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MVC框架最核心的DispatcherServlet，请求转发器
 * Created by lizhaoz on 2015/12/17.
 */

//loadOnStartup等于0或者大于0时就开始加载，当小于0时选择才加载
@WebServlet(
    urlPatterns   = "/*",
    loadOnStartup = 0
)
public class DispatcherServlet extends HttpServlet {

    /**
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param config
     *
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {

        // 初始化相关Helper类
        HelperLoader.init();

        // 获取ServletContext对象
        ServletContext servletContext = config.getServletContext();

        // 注册处理JSP的Servlet
        // 在Servlet3.0中 过滤器，监听器都可以动态注册
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");

        // 添加JSP路径映射，意思就是说/WEB-INF/view/下面的都交给jspServlet去处理
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+ "*");

        // 注册处理静态资源的默认Servlet,路径是Js,css,图片等
        // 忽略所有的静态请求的方法
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

    }

    /**
     * 1. 获取请求相关信息（请求方法与请求 URL），封装为 RequestBean。
     2. 根据 RequestBean 从 Action Map 中获取对应的 ActionBean（包括 Action 类与 Action 方法）。
     3. 解析请求 URL 中的占位符，并根据真实的 URL 生成对应的 Action 方法参数列表（Action 方法参数的顺序与 URL 占位符的顺序相同）。
     4. 根据反射创建 Action 对象，并调用 Action 方法，最终获取返回值（Result）。
     5. 将返回值转换为 JSON 格式（或者 XML 格式，可根据 Action 方法上的 @Response 注解来判断）。
     * author：Lizhao
     * Date:15/12/17
     * version:1.0
     *
     * @param request
     * @param response
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取当前请求所有相关数据
        String requestMethod=request.getMethod().toLowerCase();
        String requestPath=request.getPathInfo();
        //获取Action 处理器
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler!=null){
            //获取Controller类及其Bean类的实例
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            //创建请求参数对象
            Map<String,Object> paramMap=new HashMap<String, Object>();
            //得到所有参数名字 用于获取post的表单数据
            Enumeration<String> paramNames =request.getParameterNames();
            //有重名的参数,后面的把前面的覆盖
            while (paramNames.hasMoreElements()){
                String paramName =paramNames.nextElement();
                String paramValue=request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            //getInputStream 获取其他数据
            String body= CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                String[] params=StringUtil.splitString(body,"&");
                if(ArrayUtil.isNotEmpty(params)){
                    for(String param:params){
                        String[] array=StringUtil.splitString(param,"=");
                        //每个参数形如a="xxx"
                        if(ArrayUtil.isNotEmpty(array)&&array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param=new Param(paramMap);
            //反射调用Action方法
            Method actionMethod=handler.getActionMethod();
            Object result= ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            //处理Action方法返回值
            if(result instanceof View){
                //返回的是jsp页面
                View view= (View) result;
                String path=view.getPath();
                if(StringUtil.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        //视图的重定向
                        response.sendRedirect(request.getContextPath()+path);
                    }
                    else {
                        //Model数据的填充
                        Map<String,Object> model=view.getModel();
                        for(Map.Entry<String,Object> entry:model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                        //页面的转发
                        request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
                    }
                }
            }
            else if(result instanceof Data){
                //返回Json数据
                Data data=(Data) result;
                Object model=data.getModel();
                if(model!=null){
                    //设置响应体
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer=response.getWriter();
                    String json= JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();//强制将缓冲里面的数据写进去
                    writer.close();
                }
            }
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
