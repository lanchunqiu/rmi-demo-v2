package com.lancq.rpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/23
 **/
public class RegisterCenterImpl implements IRegisterCenter {
    private CuratorFramework client ;
    {
        client = CuratorFrameworkFactory
                .builder()
                .connectString(ZKConfig.CONNECTION_STR)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(4000)
                .build();
        client.start();
    }

    public void register(String serviceName, String serviceAddress) {
        //ע����Ӧ�ķ���
        String servicePath = ZKConfig.EK_REGISTER_PATH + "/" + serviceName;

        try {
            //�жϡ�/registers/product-service���Ƿ���ڣ������ڴ���
            if(client.checkExists().forPath(servicePath) == null){
                client.create().creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath,"0".getBytes());
            }
            String addressPath = servicePath + "/" + serviceAddress;
            String rsNode = client.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath, "0".getBytes());
            System.out.println("����ע��ɹ���" + rsNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
