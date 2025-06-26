package com.webank.wecross.stub.fabric2.rpc.methods.response;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import java.util.Map;

public class SubscribeResponse extends Response<Map<String, Object>> {
    public SubscribeResponse() {
        super();
    }

    public String getSubscribeId() {
        Map<String, Object> dataObject = getData();
        String subscribeId = (String) dataObject.get("subscribeId");
        return subscribeId;
    }
}
