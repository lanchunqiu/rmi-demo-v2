package com.lancq.rpc;

import com.lancq.rpc.annotation.RpcAnnotation;
import com.lancq.rpc.zk.IRegisterCenter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author lancq
 * @Description ���ڷ���һ��Զ�̷���
 * @Date 2018/6/18
 **/
public class RpcServer {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private IRegisterCenter registerCenter;//ע������
    private String serviceAddress;//���񷢲���ַ
    //��ŷ������ƺͷ������֮��Ĺ�ϵ
    private Map<String,Object> handlerMap = new HashMap<String,Object>();

    public RpcServer(IRegisterCenter registerCenter, String serviceAddress) {
        this.registerCenter = registerCenter;
        this.serviceAddress = serviceAddress;
    }

    /**
     * �󶨷������ƺͷ������
     * @param services
     */
    public void bind(Object... services){
        for(Object service : services){
            RpcAnnotation annotation = service.getClass().getAnnotation(RpcAnnotation.class);//
            String serviceName = annotation.value().getName();
            String version = annotation.version();
            if(version != null && !version.equals("")){
                serviceName = serviceName + "-" + version;
            }
            handlerMap.put(serviceName, service);//�󶨷���ӿ����ƶ�Ӧ�ķ���
        }
    }

    public void publish(){
        ServerSocket serverSocket = null;
        try {
            String[] addrs = serviceAddress.split(":");

            serverSocket = new ServerSocket(Integer.parseInt(addrs[1]));//����һ���������

            for(String interfaceName : handlerMap.keySet()){
                registerCenter.register(interfaceName, serviceAddress);
                System.out.println("ע�����ɹ���" + interfaceName + "->" + serviceAddress);
            }
            System.out.println(serverSocket);
            while(true){//ѭ������
                Socket socket = serverSocket.accept();//��������
                System.out.println(socket);
                //ͨ���̳߳�ȥ��������
                executorService.execute(new ProcessorHandler(socket,handlerMap));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
