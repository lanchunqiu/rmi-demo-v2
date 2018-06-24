package com.lancq.rpc.zk.loadbalance;

import java.util.List;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public abstract class AbstractLoadBalance implements ILoadBalance {
    public String selectHost(List<String> hosts) {
        if(hosts == null || hosts.size() == 0){
            return null;
        }
        if(hosts.size() == 1){
            return hosts.get(0);
        }
        return doSelect(hosts);
    }

    protected abstract String doSelect(List<String> hosts);
}
