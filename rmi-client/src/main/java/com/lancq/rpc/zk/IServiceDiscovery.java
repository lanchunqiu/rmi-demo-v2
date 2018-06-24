package com.lancq.rpc.zk;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public interface IServiceDiscovery {
    String discover(String serviceName);
}
