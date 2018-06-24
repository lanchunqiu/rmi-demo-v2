package com.lancq.rpc.zk;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public interface IRegisterCenter {
    /**
     * 注册服务名称和服务地址
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, String serviceAddress);
}
