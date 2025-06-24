package com.webank.wecross.stub.fabric2.rpc.methods.response;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.model.ContractInfo;
import java.util.List;

public class ContractsResponse extends Response<List<ContractInfo>> {
    public ContractsResponse() {
        super();
    }

    public List<ContractInfo> getContracts() {
        return getData();
    }

    public void setContracts(List<ContractInfo> contracts) {
        setData(contracts);
    }
}
