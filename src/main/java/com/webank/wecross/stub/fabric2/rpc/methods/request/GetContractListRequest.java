package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class GetContractListRequest {
    private String sdkConfig;

    public GetContractListRequest(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public void setSdkConfig(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public String getSdkConfig() {
        return this.sdkConfig;
    }
}
