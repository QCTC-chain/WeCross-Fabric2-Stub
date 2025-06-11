package com.webank.wecross.stub.fabric2.rpc.model;

public class ContractInfo {
    private String name;
    private String version;

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

    @Override
    public String toString() {
        return "ContractInfo{" + "name='" + name + "," + "version='" + version + "'" + "}";
    }
}
