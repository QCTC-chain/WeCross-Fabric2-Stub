package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class InstantiationRequest extends FabricBaseRequest {

    public InstantiationRequest(String sdkConfig) {
        super(sdkConfig);
    }

    @Override
    public String toString() {
        return "InstantiationRequest{" + "isGM=" + getIsGM() + ",isSM3=" + getIsSM3() + "}";
    }
}
