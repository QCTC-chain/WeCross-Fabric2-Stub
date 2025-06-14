package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class SubscribeEventRequest {
    private String chainName;
    private String channelId;
    private String chaincodeId;
    private String topic;
    private long fromBlock;
    private long endBlock;

    public SubscribeEventRequest(
            String chainName,
            String channelId,
            String chaincodeId,
            String topic,
            long fromBlock,
            long endBlock) {
        this.chainName = chainName;
        this.chaincodeId = chaincodeId;
        this.channelId = channelId;
        this.topic = topic;
        this.fromBlock = fromBlock;
        this.endBlock = endBlock;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getChainName() {
        return this.chainName;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChaincodeId(String chaincodeId) {
        this.chaincodeId = chaincodeId;
    }

    public String getChaincodeId() {
        return this.chaincodeId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setFromBlock(long fromBlock) {
        this.fromBlock = fromBlock;
    }

    public long getFromBlock() {
        return this.fromBlock;
    }

    public void setEndBlock(long endBlock) {
        this.endBlock = endBlock;
    }

    public long getEndBlock() {
        return this.endBlock;
    }

    @Override
    public String toString() {
        return "SubscribeEventRequest{"
                + "channelId='"
                + channelId
                + "',"
                + "chaincodeId='"
                + chaincodeId
                + "',"
                + "topic='"
                + topic
                + "',"
                + "fromBlock="
                + fromBlock
                + "endBlock="
                + endBlock
                + "}";
    }
}
