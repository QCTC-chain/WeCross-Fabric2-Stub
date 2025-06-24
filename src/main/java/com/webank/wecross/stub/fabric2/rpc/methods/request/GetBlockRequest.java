package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class GetBlockRequest {
    private String sdkConfig;
    private String blockNumber;
    private boolean onlyHeader;

    public GetBlockRequest(String sdkConfig, long blockNumber, boolean onlyHeader) {
        this.sdkConfig = sdkConfig;
        this.blockNumber = (blockNumber == -1) ? "latest" : String.format("%d", blockNumber);
        this.onlyHeader = onlyHeader;
    }

    public void setSdkConfig(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public String getSdkConfig() {
        return this.sdkConfig;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockNumber() {
        return this.blockNumber;
    }

    public void setOnlyHeader(boolean onlyHeader) {
        this.onlyHeader = onlyHeader;
    }

    public boolean isOnlyHeader() {
        return this.onlyHeader;
    }
}
