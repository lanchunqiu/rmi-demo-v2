package com.lancq.rpc;

import com.lancq.rpc.zk.IServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class RemoteInvocationHandler implements InvocationHandler {
    private IServiceDiscovery serviceDiscovery;
    private String version;
    public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version = version;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //��װ����
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(version);

        String serviceAddress = serviceDiscovery.discover(rpcRequest.getClassName());//���ݽӿ����Ƶõ���Ӧ�ķ����ַ
        //ͨ��tcp����Э����д���
        String[] temp = serviceAddress.split(":");
        TCPTransport tcpTransport = new TCPTransport(temp[0], Integer.parseInt(temp[1]));
        //��������
        return tcpTransport.send(rpcRequest);
    }
}
