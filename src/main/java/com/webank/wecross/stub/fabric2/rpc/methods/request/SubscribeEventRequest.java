package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class SubscribeEventRequest extends FabricBaseRequest {
    private String chainName;
    private String chaincodeName;
    private String eventName;
    private long fromBlock = -1;
    private long endBlock = -1;

    public SubscribeEventRequest(
            String sdkConfig,
            String chainNamea,
            String chaincodeName,
            String topic,
            long fromBlock,
            long endBlock) {
        super(sdkConfig);
        this.chainName = chainNamea;
        this.chaincodeName = chaincodeName;
        this.eventName = topic;
        this.fromBlock = fromBlock;
        this.endBlock = endBlock;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodeName() {
        return this.chaincodeName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return this.eventName;
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
                + "chainName='"
                + chainName
                + "',"
                + "chaincodeName='"
                + chaincodeName
                + "',"
                + "eventName='"
                + eventName
                + "',"
                + "fromBlock="
                + fromBlock
                + "endBlock="
                + endBlock
                + "}";
    }
}
