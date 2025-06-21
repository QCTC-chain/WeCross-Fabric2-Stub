package com.webank.wecross.stub.fabric2.rpc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.fabric2.rpc.methods.Request;
import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.methods.request.*;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractsResponse;
import com.webank.wecross.stub.fabric2.rpc.service.FabricService;

public class FabricPRCRest implements FabricRPC {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private FabricService fabricService;

    public FabricPRCRest(FabricService fabricService) {
        this.fabricService = fabricService;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public RemoteCall<Response> initConfiguration(String configuration) {
        try {
            InitConfigRequest initConfigRequest =
                    objectMapper.readValue(configuration, InitConfigRequest.class);
            Request<InitConfigRequest> request = new Request<>(initConfigRequest);
            return new RemoteCall<>(
                    fabricService, "POST", "/api/v1/config/init", Response.class, request);
        } catch (Exception e) {
            throw new RuntimeException("读取配置失败");
        }
    }

    @Override
    public RemoteCall<Response> instantiateRemoteService() {
        return new RemoteCall<>(
                fabricService,
                "POST",
                "/api/v1/service/instantiate",
                Response.class,
                new Request<>());
    }

    @Override
    public RemoteCall<Response> getBlock(GetBlockRequest blockRequest) {
        Request<GetBlockRequest> request = new Request<>(blockRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/block/info", Response.class, request);
    }

    @Override
    public RemoteCall<ContractsResponse> getContractList(String chainName, String channelId) {
        return new RemoteCall<>(
                fabricService,
                "GET",
                String.format(
                        "/api/v1/contract/list?chainName=%s&channelId=%s", chainName, channelId),
                ContractsResponse.class,
                new Request<>());
    }

    @Override
    public RemoteCall<Response> getContractInfo(GetContractInfoRequest contractInfoRequest) {
        Request<GetContractInfoRequest> request = new Request<>(contractInfoRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/contract/info", Response.class, request);
    }

    @Override
    public RemoteCall<Response> subscribeContractEvent(
            SubscribeEventRequest subscribeEventRequest) {
        Request<SubscribeEventRequest> request = new Request<>(subscribeEventRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/contract/subscribe", Response.class, request);
    }

    @Override
    public RemoteCall<Response> unSubscribeContractEvent(
            UnSubscribeEventRequest unSubscribeEventRequest) {
        Request<UnSubscribeEventRequest> request = new Request<>(unSubscribeEventRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/contract/unsubscribe", Response.class, request);
    }

    @Override
    public RemoteCall<Response> call(FabricTransactionRequest transactionRequest) {
        Request<FabricTransactionRequest> request = new Request<>(transactionRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/contract/call", Response.class, request);
    }

    @Override
    public RemoteCall<Response> sendTransaction(FabricTransactionRequest transactionRequest) {
        Request<FabricTransactionRequest> request = new Request<>(transactionRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/contract/sendTransaction", Response.class, request);
    }

    @Override
    public RemoteCall<Response> getTransactionInfo(GetTransactionRequest transactionRequest) {
        Request<GetTransactionRequest> request = new Request<>(transactionRequest);
        return new RemoteCall<>(
                fabricService, "POST", "/api/v1/transaction/info", Response.class, request);
    }
}
