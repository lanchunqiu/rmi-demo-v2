package com.lancq.rpc;

import com.lancq.rpc.zk.IServiceDiscovery;

import java.lang.reflect.Proxy;

/**
 * @Author lancq
 * @Description �����ͻ��˵�Զ�̴���ͨ��Զ�̴�����з���
 * @Date 2018/6/18
 **/
public class RpcClientProxy {
    private IServiceDiscovery serviceDiscovery;

    public RpcClientProxy(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public<T> T clientProxy(final Class<T> interfacecls, String version){
        //ʹ�õ��˶�̬����
        return (T)Proxy.newProxyInstance(interfacecls.getClassLoader(),
                new Class[] {interfacecls},
                new RemoteInvocationHandler(serviceDiscovery, version));
    }
}
