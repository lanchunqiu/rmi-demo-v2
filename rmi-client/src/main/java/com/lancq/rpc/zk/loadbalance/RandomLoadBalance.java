package com.lancq.rpc.zk.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public class RandomLoadBalance extends AbstractLoadBalance {
    protected String doSelect(List<String> hosts) {
        int len=hosts.size();
        Random random=new Random();
        return hosts.get(random.nextInt(len));

    }
}
