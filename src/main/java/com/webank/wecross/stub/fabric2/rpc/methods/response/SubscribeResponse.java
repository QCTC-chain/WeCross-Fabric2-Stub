package com.webank.wecross.stub.fabric2.rpc.methods.response;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import java.util.Map;

public class SubscribeResponse extends Response<Map<String, String>> {
    public SubscribeResponse() {
        super();
    }

    public String getSubscribeId() {
        String subscribeId = getData().get("subscribeId");
        return subscribeId;
    }
}
