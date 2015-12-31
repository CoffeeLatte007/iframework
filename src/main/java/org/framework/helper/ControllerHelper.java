package org.framework.helper;

import org.framework.bean.Handler;
import org.framework.bean.Request;
import org.framework.util.ArrayUtil;
import org.framework.util.CollectionUtil;
import org.framework.annotation.Action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * 控制器助手类
 * Created by lizhaoz on 2015/12/17.
 */

public final class ControllerHelper {
    //用于放请求和处理器的映射关系，@Action()中的值就是 请求
    private static final Map<Request,Handler> ACTION_MAP=new HashMap<Request, Handler>();
    static {
        //得到所有有控制器注解的类
        Set<Class<?>> controllerClassSet=ClassHelper.getControllerClassSet();
        //判断是否为空
        if(CollectionUtil.isNotEmpty(controllerClassSet)){
            //遍历控制器注解的类
            for(Class<?> controllerClass:controllerClassSet){
                //获取Controller类的所有方法(不包括继承的) getMethods是返回public方法(包括继承的)
                Method[] methods=controllerClass.getDeclaredMethods();
                //判断手否为空
                if(ArrayUtil.isNotEmpty(methods)){
                    //遍历
                    for (Method method:methods){
                        //判断该方法是否带有Action注解
                        if(method.isAnnotationPresent(Action.class)){
                            //如果有，就获取注解
                            Action action=method.getAnnotation(Action.class);
                            //得到URL的值,例如get:/customer
                            String mapping=action.value();
                            //匹配URL映射规则，
                            if (mapping.matches("\\w+:/\\w*")){
                                //把get:/customer切分成两段
                                String[] array=mapping.split(":");
                                if(ArrayUtil.isNotEmpty(array)&&array.length==2);{
                                    //设置请求方法和请求路径
                                    Request request=new Request(array[0],array[1]);
                                    //设置actionBean
                                    Handler actionBean=new Handler(controllerClass,method);
                                    //向映射得MAP中添加请求和 处理器方法
                                    ACTION_MAP.put(request,actionBean);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request=new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
