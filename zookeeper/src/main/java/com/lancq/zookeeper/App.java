package com.lancq.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class App {
    public static void main(String[] args) throws IOException {
        CountDownLatch countDownLatch=new CountDownLatch(10);
        for(int i=0;i<10;i++){
            new Thread(new Runnable(){
                public void run(){
                    try {
                        countDownLatch.await();
                        DistributedLock distributedLock = new DistributedLock();
                        distributedLock.lock(); //»ñµÃËø
                        Thread.sleep(5000);
                        distributedLock.unlock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"Thread-"+i).start();
            countDownLatch.countDown();
        }
        //System.in.read();

    }
}
