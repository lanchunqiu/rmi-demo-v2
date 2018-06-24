package com.lancq.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class ClientDemo {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IHelloService helloService = (IHelloService)Naming.lookup("rmi://127.0.0.1/Hello");

        System.out.println(helloService.sayHello("lancq"));
    }
}
