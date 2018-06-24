package com.lancq.rpc;


import com.lancq.rpc.zk.IServiceDiscovery;
import com.lancq.rpc.zk.ServiceDiscoveryImpl;
import com.lancq.rpc.zk.ZKConfig;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class ClientDemo {
    public static void main(String[] args) {
        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl(ZKConfig.CONNECTION_STR);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);
        IHello hello = rpcClientProxy.clientProxy(IHello.class, "2.0");

        System.out.println(hello.sayHello("lancq"));
    }
}
