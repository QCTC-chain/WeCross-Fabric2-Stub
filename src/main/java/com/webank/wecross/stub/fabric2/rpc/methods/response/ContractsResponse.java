package com.webank.wecross.stub.fabric2.rpc.methods.response;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.model.Contracts;

public class ContractsResponse extends Response<Contracts> {
    public ContractsResponse() {
        super();
    }

    public Contracts getContracts() {
        return getData();
    }

    public void setContracts(Contracts contracts) {
        setData(contracts);
    }
}
