package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class UnSubscribeEventRequest extends FabricBaseRequest {
    private String subscribeEventId;

    public UnSubscribeEventRequest(String sdkConfig, String subscribeEventId) {
        super(sdkConfig);
        this.subscribeEventId = subscribeEventId;
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
