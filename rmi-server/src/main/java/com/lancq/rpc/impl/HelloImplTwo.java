package com.lancq.rpc.impl;

import com.lancq.rpc.IHello;
import com.lancq.rpc.annotation.RpcAnnotation;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
@RpcAnnotation(IHello.class)
public class HelloImplTwo implements IHello {
    public String sayHello(String msg) {
        return "I'm 8982 node," + msg;
    }
}
