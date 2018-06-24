package com.lancq.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author lancq
 * @Description
 * @Date 2018/6/18
 **/
public class TCPTransport {
    private String host;
    private int port;

    public TCPTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }
    //创建一个socket连接
    private Socket newSocket(){
        System.out.println("创建一个新的连接");
        Socket socket;
        try {
            socket = new Socket(host, port);
            System.out.println(socket);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException("连接建立失败");
        }
    }

    public Object send(RpcRequest rpcRequest){
        Socket socket = null;
        ObjectOutputStream objectOutputStream =null;
        ObjectInputStream objectInputStream = null;
        try{
            socket = newSocket();
            //获取输出流，将客户端需要调用的远程方法参数request发送给
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            //获取输入流，得到服务端的返回结果
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object result = objectInputStream.readObject();
            return result;
        } catch (Exception e){
            throw new RuntimeException("发起远程调用异常：" + e);
        } finally {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null ){
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
        }

    }
}
