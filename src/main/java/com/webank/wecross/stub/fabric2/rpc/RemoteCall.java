package com.webank.wecross.stub.fabric2.rpc;

import com.webank.wecross.stub.fabric2.rpc.methods.Callback;
import com.webank.wecross.stub.fabric2.rpc.methods.Request;
import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.service.FabricService;

public class RemoteCall<T extends Response> {
    private FabricService fabricService;
    private String httpMethod;
    private String uri;
    private Class<T> responseType;
    private Request<?> request;

    public RemoteCall(
            FabricService fabricService,
            String httpMethod,
            String uri,
            Class<T> responseType,
            Request<?> request) {
        this.fabricService = fabricService;
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.responseType = responseType;
        this.request = request;
    }

    public T send() throws Exception {
        return fabricService.send(httpMethod, uri, request, responseType);
    }

    public void asyncSend(Callback<T> callback) {
        fabricService.asyncSend(httpMethod, uri, request, responseType, callback);
    }

    public FabricService getWeCrossService() {
        return fabricService;
    }

    public void setWeCrossService(FabricService fabricService) {
        this.fabricService = fabricService;
    }

    public Class<T> getResponseType() {
        return responseType;
    }

    public void setResponseType(Class<T> responseType) {
        this.responseType = responseType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
