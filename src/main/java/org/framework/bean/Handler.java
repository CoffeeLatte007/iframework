package org.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装Action信息
 * Created by lizhaoz on 2015/12/17.
 */

public class Handler {
    private Class<?> controllerClass;
    //Action方法信息
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
