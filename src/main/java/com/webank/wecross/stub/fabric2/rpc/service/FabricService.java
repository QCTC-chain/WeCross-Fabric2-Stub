package com.webank.wecross.stub.fabric2.rpc.service;

import com.webank.wecross.stub.fabric2.rpc.methods.Callback;
import com.webank.wecross.stub.fabric2.rpc.methods.Request;
import com.webank.wecross.stub.fabric2.rpc.methods.Response;

public interface FabricService {
    void init(String server) throws Exception;

    <T extends Response> T send(
            String httpMethod, String uri, Request request, Class<T> responseType) throws Exception;

    <T extends Response> void asyncSend(
            String httpMethod,
            String uri,
            Request<?> request,
            Class<T> responseType,
            Callback<T> callback);
}
