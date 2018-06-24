package com.lancq.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public interface IHelloService extends Remote {
    String sayHello(String msg) throws RemoteException;
}
