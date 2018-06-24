package com.lancq.rpc;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author lancq
 * @Description ´«Êä¶ÔÏó
 * @Date 2018/6/18
 **/
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 6192139244640485330L;
    private String className;
    private String methodName;
    private Object[] parameters;
    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    @Override
    public String toString() {
        return "RpcRequest{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
