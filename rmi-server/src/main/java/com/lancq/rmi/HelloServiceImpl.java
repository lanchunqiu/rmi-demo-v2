package com.lancq.rmi;

import com.lancq.rmi.IHelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class HelloServiceImpl extends UnicastRemoteObject implements IHelloService {
    public HelloServiceImpl() throws RemoteException {
        super();
    }

    public String sayHello(String msg) throws RemoteException {
        return "Hello," + msg;
    }
}
