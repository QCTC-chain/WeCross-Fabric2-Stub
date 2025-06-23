package com.webank.wecross.stub.fabric2.rpc.methods.response;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import java.util.Map;

public class ContractResultResponse extends Response<Object> {

    public ContractResultResponse() {
        super();
    }

    public String getPayload() {
        Map<String, String> payload = (Map<String, String>) getData();
        return payload.get("payload");
    }

    public void setPayload(Object payload) {
        setData(payload);
    }
}
