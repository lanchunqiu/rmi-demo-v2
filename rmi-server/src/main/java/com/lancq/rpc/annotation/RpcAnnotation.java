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
     * ���ⷢ���ķ���Ľӿڵ�ַ
     * @return
     */
    Class<?> value();

    String version() default "";
}
