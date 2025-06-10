package com.webank.wecross.stub.fabric2.rpc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.fabric2.rpc.methods.Request;
import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.methods.request.InitConfigRequest;
import com.webank.wecross.stub.fabric2.rpc.service.FabricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricPRCRest implements FabricRPC {
    private final Logger logger = LoggerFactory.getLogger(FabricPRCRest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private FabricService fabricService;

    public FabricPRCRest(FabricService fabricService) {
        this.fabricService = fabricService;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public RemoteCall<Response> InitConfiguration(String configuration) throws Exception {
        InitConfigRequest initConfigRequest =
                objectMapper.readValue(configuration, InitConfigRequest.class);
        Request<InitConfigRequest> request = new Request<>(initConfigRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/config/init", Response.class, request);
    }

    @Override
    public RemoteCall<Response> instantiateRemoteService() throws Exception {
        return new RemoteCall<>(
                fabricService,
                "POST",
                "/api/v1/service/instantiate",
                Response.class,
                new Request<>());
    }
}
