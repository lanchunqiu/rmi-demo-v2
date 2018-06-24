package com.lancq.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class DistributedLock implements Lock,Watcher {
    private ZooKeeper zk;
    private String ROOT_LOCK = "/locks";//定义根节点
    private String WAIT_LOCK ;//等待前一个锁
    private String CURRENT_LOCK;//当前锁

    private CountDownLatch countDownLatch;

    public DistributedLock() {
        try {
            zk = new ZooKeeper("192.168.227.129:2181,192.168.227.130:2181,192.168.227.131:2181",4000,this);
            Stat stat = zk.exists(ROOT_LOCK, false);

            if(stat == null){
                zk.create(ROOT_LOCK,"0".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void lock() {
        if(this.tryLock()){//如果获得锁成功
            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+"->获得锁成功");
            return;
        } else {
            try {
                waitForLock(WAIT_LOCK); //没有获得锁，继续等待获得锁
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForLock(String prev) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(prev,true);//监听当前节点的上一个节点
        if(stat != null){
            System.out.println(Thread.currentThread().getName()+"->等待锁"+prev+"释放");
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            countDownLatch = null;

            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+"->获得锁成功");

        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        System.out.println("11111111");
        this.lock();
    }

    @Override
    public boolean tryLock() {
        try {
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println(Thread.currentThread().getName()+"->"+ CURRENT_LOCK+"，尝试竞争锁");

            List<String> children = zk.getChildren(ROOT_LOCK, false);//获取根节点下的所有子节点

            SortedSet<String> sortedSet = new TreeSet();//定义一个集合进行排序

            for(String ch : children){
                sortedSet.add(ROOT_LOCK + "/" + ch);
            }

            String firstNode = sortedSet.first();//获得当前所有子节点中最小的节点

            SortedSet<String> lessThanMe = ((TreeSet<String>) sortedSet).headSet(CURRENT_LOCK);
            //System.out.println(CURRENT_LOCK+":"+lessThanMe);
            if(CURRENT_LOCK.equals(firstNode)){//通过当前的节点和子节点中最小的节点进行比较，如果相等，表示获得锁成功
                return true;
            }

            if(!lessThanMe.isEmpty()){
                WAIT_LOCK = lessThanMe.last();//获得比当前节点更小的最后一个节点，设置给WAIT_LOCK
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        System.out.println(Thread.currentThread().getName()+"->释放锁"+CURRENT_LOCK);
        try {
            zk.delete(CURRENT_LOCK,-1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("event = [" + event + "]");
        if(event.getType() != Event.EventType.NodeDeleted){
            return;
        }

        //触发以后，还需要再次判断当前等待的节点是不是最小的
        List<String> children = null;//获取根节点下的所有子节点
        try {
            children = zk.getChildren(ROOT_LOCK, false);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SortedSet<String> sortedSet = new TreeSet();//定义一个集合进行排序

        for(String ch : children){
            sortedSet.add(ROOT_LOCK + "/" + ch);
        }

        String firstNode = sortedSet.first();//获得当前所有子节点中最小的节点

        SortedSet<String> lessThanMe = ((TreeSet<String>) sortedSet).headSet(CURRENT_LOCK);
        if(CURRENT_LOCK.equals(firstNode)){
            if(this.countDownLatch!=null){
                this.countDownLatch.countDown();
            }
        }

    }
}
