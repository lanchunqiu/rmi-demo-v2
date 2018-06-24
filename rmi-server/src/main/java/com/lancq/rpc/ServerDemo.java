package com.lancq.rpc;

import com.lancq.rpc.impl.HelloImpl;
import com.lancq.rpc.impl.HelloImpl2;
import com.lancq.rpc.zk.IRegisterCenter;
import com.lancq.rpc.zk.RegisterCenterImpl;

import java.io.IOException;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class ServerDemo {
    public static void main(String[] args) throws IOException {
        IHello hello = new HelloImpl();
        IHello hello2 = new HelloImpl2();
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        RpcServer rpcServer = new RpcServer(registerCenter,"127.0.0.1:8989");
        rpcServer.bind(hello,hello2);
        rpcServer.publish();
        System.in.read();
    }
}
