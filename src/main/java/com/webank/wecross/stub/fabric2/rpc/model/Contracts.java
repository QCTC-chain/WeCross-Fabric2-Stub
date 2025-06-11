package com.webank.wecross.stub.fabric2.rpc.model;

import java.util.Arrays;

public class Contracts {
    private ContractInfo[] contractInfos;

    public ContractInfo[] getContractInfos() {
        return contractInfos;
    }

    public void setContractInfos(ContractInfo[] contractInfos) {
        this.contractInfos = contractInfos;
    }

    @Override
    public String toString() {
        return "Contracts{" + "contractInfos=" + Arrays.toString(contractInfos) + "}";
    }
}
