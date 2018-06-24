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
    private String ROOT_LOCK = "/locks";//������ڵ�
    private String WAIT_LOCK ;//�ȴ�ǰһ����
    private String CURRENT_LOCK;//��ǰ��

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
        if(this.tryLock()){//���������ɹ�
            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+"->������ɹ�");
            return;
        } else {
            try {
                waitForLock(WAIT_LOCK); //û�л�����������ȴ������
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForLock(String prev) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(prev,true);//������ǰ�ڵ����һ���ڵ�
        if(stat != null){
            System.out.println(Thread.currentThread().getName()+"->�ȴ���"+prev+"�ͷ�");
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            countDownLatch = null;

            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+"->������ɹ�");

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

            System.out.println(Thread.currentThread().getName()+"->"+ CURRENT_LOCK+"�����Ծ�����");

            List<String> children = zk.getChildren(ROOT_LOCK, false);//��ȡ���ڵ��µ������ӽڵ�

            SortedSet<String> sortedSet = new TreeSet();//����һ�����Ͻ�������

            for(String ch : children){
                sortedSet.add(ROOT_LOCK + "/" + ch);
            }

            String firstNode = sortedSet.first();//��õ�ǰ�����ӽڵ�����С�Ľڵ�

            SortedSet<String> lessThanMe = ((TreeSet<String>) sortedSet).headSet(CURRENT_LOCK);
            //System.out.println(CURRENT_LOCK+":"+lessThanMe);
            if(CURRENT_LOCK.equals(firstNode)){//ͨ����ǰ�Ľڵ���ӽڵ�����С�Ľڵ���бȽϣ������ȣ���ʾ������ɹ�
                return true;
            }

            if(!lessThanMe.isEmpty()){
                WAIT_LOCK = lessThanMe.last();//��ñȵ�ǰ�ڵ��С�����һ���ڵ㣬���ø�WAIT_LOCK
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
        System.out.println(Thread.currentThread().getName()+"->�ͷ���"+CURRENT_LOCK);
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

        //�����Ժ󣬻���Ҫ�ٴ��жϵ�ǰ�ȴ��Ľڵ��ǲ�����С��
        List<String> children = null;//��ȡ���ڵ��µ������ӽڵ�
        try {
            children = zk.getChildren(ROOT_LOCK, false);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SortedSet<String> sortedSet = new TreeSet();//����һ�����Ͻ�������

        for(String ch : children){
            sortedSet.add(ROOT_LOCK + "/" + ch);
        }

        String firstNode = sortedSet.first();//��õ�ǰ�����ӽڵ�����С�Ľڵ�

        SortedSet<String> lessThanMe = ((TreeSet<String>) sortedSet).headSet(CURRENT_LOCK);
        if(CURRENT_LOCK.equals(firstNode)){
            if(this.countDownLatch!=null){
                this.countDownLatch.countDown();
            }
        }

    }
}
