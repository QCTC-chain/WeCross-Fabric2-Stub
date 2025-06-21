package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class GetContractInfoRequest {
    private String sdkConfig;
    private String chaincodeName;

    public GetContractInfoRequest(String sdkConfig, String chaincodeName) {
        this.sdkConfig = sdkConfig;
        this.chaincodeName = chaincodeName;
    }

    public void setSdkConfig(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public String getSdkConfig() {
        return this.sdkConfig;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodeName() {
        return this.chaincodeName;
    }
}
