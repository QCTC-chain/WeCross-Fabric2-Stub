package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class FabricBaseRequest {
    private String sdkConfig;
    private boolean isGM;
    private boolean isSM3;

    public FabricBaseRequest(String sdkConfig) {
        this.sdkConfig = sdkConfig;
        this.isGM = false;
        this.isSM3 = false;
    }

    public void setSdkConfig(String sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    public String getSdkConfig() {
        return this.sdkConfig;
    }

    public void setGM(boolean isGM) {
        this.isGM = isGM;
    }

    public boolean isGM() {
        return this.isGM;
    }

    public void setSM3(boolean isSM3) {
        this.isSM3 = isSM3;
    }

    public boolean isSM3() {
        return this.isSM3;
    }
}
