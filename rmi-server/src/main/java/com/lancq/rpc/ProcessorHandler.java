package com.lancq.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class ProcessorHandler implements Runnable {
    private Socket socket;

    private Map<String,Object> handlerMap;//服务端发布的服务

    public ProcessorHandler(Socket socket, Map<String,Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    public void run() {
        //处理请求
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            //获取客户端的输入流
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            //反序列化远程传输的对象RpcRequest
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            System.out.println("rpcRequest:" + rpcRequest);
            Object result = invoke(rpcRequest);//通过反射去调用本地的方法

            //通过输出流讲结果输出给客户端
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest rpcRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //一下均为反射操作，目的是通过反射调用服务
        Object[] args = rpcRequest.getParameters();
        Class<?>[] types = new Class[args.length];
        for(int i=0; i<args.length; i++){
            types[i] = args[i].getClass();
        }
        String serviceName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();
        if(version != null && !version.equals("")){
            serviceName = serviceName + "-" + version;
        }
        //从handlerMap中，根据客户端请求的地址，取拿到响应的服务，通过反射调用
        Object service = handlerMap.get(serviceName);
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(),types);
        return method.invoke(service, args);
    }
}
