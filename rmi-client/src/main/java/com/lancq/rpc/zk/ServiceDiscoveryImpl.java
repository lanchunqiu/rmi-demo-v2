package com.lancq.rpc.zk;

import com.lancq.rpc.zk.loadbalance.ILoadBalance;
import com.lancq.rpc.zk.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public class ServiceDiscoveryImpl implements IServiceDiscovery{
    private List<String> repos=new ArrayList<String>();
    private CuratorFramework client ;

    public ServiceDiscoveryImpl(String zkAddress){
        client = CuratorFrameworkFactory
                .builder()
                .connectString(zkAddress)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(4000)
                .build();
        client.start();
    }
    public String discover(String serviceName) {
        String path = ZKConfig.EK_REGISTER_PATH + "/" + serviceName;
        try {
            repos = client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("或取子节点异常" + e);
        }
        //动态发现服务节点的变化
        registerWatcher(path);

        //负载均衡
        ILoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.selectHost(repos);
    }

    private void registerWatcher(final String path){
        final PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                repos = client.getChildren().forPath(path);
            }
        };
        childrenCache.getListenable().addListener(listener);

        try {
            childrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
