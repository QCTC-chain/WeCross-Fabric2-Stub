package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class UnSubscribeEventRequest {
    private String channelId;
    private String subscribeEventId;

    public UnSubscribeEventRequest(String channelId, String subscribeEventId) {
        this.channelId = channelId;
        this.subscribeEventId = subscribeEventId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setSubscribeEventId(String subscribeEventId) {
        this.subscribeEventId = subscribeEventId;
    }

    public String getSubscribeEventId() {
        return this.subscribeEventId;
    }

    @Override
    public String toString() {
        return "UnSubscribeEventRequest{"
                + "channelId='"
                + channelId
                + "',"
                + "subscribeEventId='"
                + subscribeEventId
                + "'"
                + "}";
    }
}
