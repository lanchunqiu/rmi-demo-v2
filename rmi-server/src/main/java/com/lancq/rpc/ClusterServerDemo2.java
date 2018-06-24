package com.lancq.rpc;

import com.lancq.rpc.impl.HelloImplTwo;
import com.lancq.rpc.zk.IRegisterCenter;
import com.lancq.rpc.zk.RegisterCenterImpl;

import java.io.IOException;

/**
 * @Author lancq
 * @Description ¼¯Èºdemo2
 * @Date 2018/6/18
 **/
public class ClusterServerDemo2 {
    public static void main(String[] args) throws IOException {
        IHello hello = new HelloImplTwo();
        IRegisterCenter registerCenter = new RegisterCenterImpl();
        RpcServer rpcServer = new RpcServer(registerCenter,"127.0.0.1:8982");
        rpcServer.bind(hello);
        rpcServer.publish();
        System.in.read();
    }
}
