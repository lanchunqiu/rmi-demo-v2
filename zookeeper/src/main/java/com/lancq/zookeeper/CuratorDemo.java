package com.lancq.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @Author lancq
 * @Description Curator分布式锁
 * @Date 2018/6/23
 **/
public class CuratorDemo {
    static final String lock_path = "/curator_locks";
    public static void main(String[] args) throws IOException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);//重试策略


        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.227.129:2181,192.168.227.130:2181,192.168.227.131:2181")
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client,"/locks");
        CountDownLatch latch = new CountDownLatch(10);
        for(int i=0; i<10; i++){
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try{
                            latch.await();
                            lock.acquire();//获得锁
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss|SSS");
                            System.out.println("生成订单号：" + sdf.format(new Date()));
                            lock.release();//分布式锁
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            ).start();
            latch.countDown();
        }

    }
}
