package com.webank.wecross.stub.fabric2.rpc.methods;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Request<T> {
    private String version = "1";
    private T data;

    @JsonIgnore private Object ext;

    public Request() {}

    public Request(T data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        String result = "Request{" + "version='" + version + '\'';
        result += (data == null) ? "" : ", data=" + data;
        result += '}';
        return result;
    }
}
