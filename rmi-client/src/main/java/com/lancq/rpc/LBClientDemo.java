package com.lancq.rpc;


import com.lancq.rpc.zk.IServiceDiscovery;
import com.lancq.rpc.zk.ServiceDiscoveryImpl;
import com.lancq.rpc.zk.ZKConfig;

/**
 * @Author lancq
 * @Description 集群负载均衡调用demo
 * @Date 2018/6/18
 **/
public class LBClientDemo {
    public static void main(String[] args) throws InterruptedException {
        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl(ZKConfig.CONNECTION_STR);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);

        for(int i=0; i<10; i++){
            IHello hello = rpcClientProxy.clientProxy(IHello.class, null);
            System.out.println(hello.sayHello("lancq"));
            Thread.sleep(1000);
        }

    }
}
