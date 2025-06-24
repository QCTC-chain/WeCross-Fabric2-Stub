package com.webank.wecross.stub.fabric2.rpc.model;

public class ContractInfo {
    private String name;
    private String version;
    private long sequence;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public long getSequence() {
        return this.sequence;
    }

    @Override
    public String toString() {
        return "ContractInfo{"
                + "name='"
                + name
                + ","
                + "version='"
                + version
                + "',sequence="
                + sequence
                + "}";
    }
}
