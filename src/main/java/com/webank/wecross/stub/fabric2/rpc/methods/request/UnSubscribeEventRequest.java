package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class UnSubscribeEventRequest {
    private String sdkConfig;
    private String subscribeEventId;

    public UnSubscribeEventRequest(String sdkConfig, String subscribeEventId) {
        this.sdkConfig = sdkConfig;
        this.subscribeEventId = subscribeEventId;
    }

    public void setSdkConfig(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public String getSdkConfig() {
        return this.sdkConfig;
    }

    public void setSubscribeEventId(String subscribeEventId) {
        this.subscribeEventId = subscribeEventId;
    }

    public String getSubscribeEventId() {
        return this.subscribeEventId;
    }

    @Override
    public String toString() {
        return "UnSubscribeEventRequest{" + "subscribeEventId='" + subscribeEventId + "'" + "}";
    }
}
