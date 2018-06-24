package com.lancq.rpc.impl;

import com.lancq.rpc.IHello;
import com.lancq.rpc.annotation.RpcAnnotation;

/**
 * @Author lancq
 * @Description ¶à°æ±¾demo
 * @Date 2018/6/18
 **/
@RpcAnnotation(value = IHello.class,version = "2.0")
public class HelloImpl2 implements IHello {
    public String sayHello(String msg) {
        return "[I'm version 2.0]Hello," + msg;
    }
}
