package com.lancq.rpc.zk;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public interface IRegisterCenter {
    /**
     * ע��������ƺͷ����ַ
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, String serviceAddress);
}
