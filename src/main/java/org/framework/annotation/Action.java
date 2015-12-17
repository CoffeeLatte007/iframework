package org.framework.annotation;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Action方法注解
 *
 * Created by lizhaoz on 2015/12/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    //请求类型和路径
    //例如delete:/customer_edit
    String value();
}

//~ Formatted by Jindent --- http://www.jindent.com
