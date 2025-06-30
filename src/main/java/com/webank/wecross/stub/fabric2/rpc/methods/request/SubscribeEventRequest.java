package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class SubscribeEventRequest extends FabricBaseRequest {
    private String chainName;
    private String chaincodeName;
    private String eventName;
    private String fromBlock = "latest";
    private String endBlock = "latest";

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
        this.fromBlock = fromBlock == -1 ? "latest" : String.format("%d", fromBlock);
        this.endBlock = endBlock == -1 ? "latest" : String.format("%d", endBlock);
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

    public void setFromBlock(String fromBlock) {
        this.fromBlock = fromBlock;
    }

    public String getFromBlock() {
        return this.fromBlock;
    }

    public void setEndBlock(String endBlock) {
        this.endBlock = endBlock;
    }

    public String getEndBlock() {
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
