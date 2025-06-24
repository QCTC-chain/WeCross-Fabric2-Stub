package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class GetContractInfoRequest extends FabricBaseRequest {
    private String chaincodeName;

    public GetContractInfoRequest(String sdkConfig, String chaincodeName) {
        super(sdkConfig);
        this.chaincodeName = chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodeName() {
        return this.chaincodeName;
    }
}
