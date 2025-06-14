package com.webank.wecross.stub.fabric2.rpc.methods.request;

public class FabricTransactionRequest {
    private String chainName;
    private String channelId;
    private String chaincodeId;
    private String method;
    private Object[] args;

    public FabricTransactionRequest(
            String chainName, String channelId, String chaincodeId, String method, Object[] args) {
        this.chainName = chainName;
        this.channelId = channelId;
        this.chaincodeId = chaincodeId;
        this.method = method;
        this.args = args;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getChainName() {
        return this.chainName;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChaincodeId(String chaincodeId) {
        this.chaincodeId = chaincodeId;
    }

    public String getChaincodeId() {
        return this.chaincodeId;
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
                + "channelId='"
                + channelId
                + "',"
                + "chaincodeId='"
                + chaincodeId
                + "',"
                + "method='"
                + method
                + "',"
                + "args="
                + (args != null ? args.toString() : "")
                + "}";
    }
}
