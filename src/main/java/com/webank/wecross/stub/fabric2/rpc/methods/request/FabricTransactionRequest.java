package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class FabricTransactionRequest extends FabricBaseRequest {
    private String chaincodeName;
    private String method;
    private Object[] args;

    public FabricTransactionRequest(
            String sdkConfig, String chaincodeName, String method, Object[] args) {
        super(sdkConfig);
        this.chaincodeName = chaincodeName;
        this.method = method;
        this.args = args;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodeName() {
        return this.chaincodeName;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object[] getArgs() {
        return this.args;
    }

    @Override
    public String toString() {
        return "FabricTransactionRequest{"
                + "chaincodeName='"
                + chaincodeName
                + "',"
                + "method='"
                + method
                + "',"
                + "args="
                + (args != null ? args.toString() : "")
                + "}";
    }
}
