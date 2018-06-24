package com.lancq.rpc.zk.loadbalance;

import java.util.List;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public interface ILoadBalance {
    String selectHost(List<String> hosts);
}
