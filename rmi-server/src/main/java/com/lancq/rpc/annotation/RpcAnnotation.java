package com.lancq.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcAnnotation {
    /**
     * 对外发布的服务的接口地址
     * @return
     */
    Class<?> value();

    String version() default "";
}
