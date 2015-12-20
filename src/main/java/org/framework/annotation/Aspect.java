package org.framework.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 * 只能作用于类上，运行时
 * Created by lizhaoz on 2015/12/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
