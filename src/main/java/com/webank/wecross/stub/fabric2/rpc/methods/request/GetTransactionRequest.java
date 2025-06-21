package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class GetTransactionRequest {
    private String sdkConfig;
    private String txId;
    private long blockNumber;
    private boolean isVerified;

    public GetTransactionRequest(
            String sdkConfig, String txId, long blockNumber, boolean isVerified) {
        this.sdkConfig = sdkConfig;
        this.txId = txId;
        this.blockNumber = blockNumber;
        this.isVerified = isVerified;
    }

    public String getSdkConfig() {
        return sdkConfig;
    }

    public String getTxId() {
        return txId;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setSdkConfig(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
