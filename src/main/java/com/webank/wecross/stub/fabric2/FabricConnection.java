package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.Toml;
import com.webank.wecross.stub.Connection;
import com.webank.wecross.stub.Request;
import com.webank.wecross.stub.ResourceInfo;
import com.webank.wecross.stub.Response;
import com.webank.wecross.stub.fabric2.common.FabricType;
import com.webank.wecross.stub.fabric2.rpc.FabricPRCRest;
import com.webank.wecross.stub.fabric2.rpc.methods.request.*;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractsResponse;
import com.webank.wecross.stub.fabric2.rpc.model.ContractInfo;
import com.webank.wecross.stub.fabric2.rpc.model.Contracts;
import com.webank.wecross.stub.fabric2.rpc.service.FabricRPCService;
import com.webank.wecross.stub.fabric2.rpc.service.FabricService;
import com.webank.wecross.stub.fabric2.utils.ConfigUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** @Description: fabric 链接对象，用于链接fabric链 发起交易 @Author: mirsu @Date: 2020/10/30 10:52 */
public class FabricConnection implements Connection {
    private Logger logger = LoggerFactory.getLogger(FabricConnection.class);

    private static ObjectMapper objectMapper = new ObjectMapper();
    private FabricPRCRest fabricPRCRest;
    private ThreadPoolTaskExecutor threadPool;
    private Map<String, String> properties = new HashMap<>();
    private String stubPath;

    private ConnectionEventHandler connectionEventHandler;
    private Toml stubToml;

    public FabricConnection(String stubPath, ThreadPoolTaskExecutor threadPool) {
        this.threadPool = threadPool;
        this.stubPath = stubPath;
    }

    // 链接初始化
    public void start() throws Exception {
        FabricService fabricService = new FabricRPCService();
        fabricService.init();
        fabricPRCRest = new FabricPRCRest(fabricService);

        // 启动远程服务
        com.webank.wecross.stub.fabric2.rpc.methods.Response response =
                fabricPRCRest.instantiateRemoteService().send();
        if (response.getErrorCode() != 0) {
            throw new RuntimeException("实例化 Fabric2 服务失败");
        }
        threadPool.initialize();
        this.stubToml = ConfigUtils.getToml(stubPath + File.separator + "stub.toml");
    }

    private String getChannelId() {
        return this.stubToml.getString("fabricServices.channelName");
    }

    private String getStubType() {
        return this.stubToml.getString("common.type");
    }

    private String getChainName() {
        return this.stubToml.getString("common.name");
    }

    private Response send(Request request) {

        switch (request.getType()) {
            case FabricType.ConnectionMessage.FABRIC_GET_BLOCK:
                return handleGetBlock(request);
            case FabricType.ConnectionMessage.FABRIC_GET_TRANSACTION:
                return handleGetTransaction(request);
            case FabricType.ConnectionMessage.FABRIC_SUBSCRIBE_CONTRACT:
                return handleSubscribeContractEvent(request);
            case FabricType.ConnectionMessage.FABRIC_UNSUBSCRIBE_CONTRACT:
                return handleUnSubscribeContractEvent(request);
            case FabricType.ConnectionMessage.FABRIC_REGISTER_EXISTING_CONTRACT:
                return handleRegisterExistingContract(request);
            default:
                return FabricConnectionResponse.build()
                        .errorCode(FabricType.TransactionResponseStatus.ILLEGAL_REQUEST_TYPE)
                        .errorMessage("Illegal request type: " + request.getType());
        }
    }

    @Override
    public void asyncSend(Request request, Connection.Callback callback) {

        switch (request.getType()) {
            case FabricType.ConnectionMessage.FABRIC_CALL:
                handleAsyncCall(request, callback);
                break;
            case FabricType.ConnectionMessage.FABRIC_SENDTRANSACTION:
                handleAsyncSendTransaction(request, callback);
                break;
            default:
                callback.onResponse(send(request));
        }
    }

    private void handleAsyncCall(Request request, Connection.Callback callback) {
        threadPool.execute(
                () -> {
                    callback.onResponse(handleTransaction(request, true));
                });
    }

    private void handleAsyncSendTransaction(Request request, Connection.Callback callback) {
        threadPool.execute(
                () -> {
                    callback.onResponse(handleTransaction(request, false));
                });
    }

    private Response handleTransaction(Request request, boolean isEvaluate) {
        try {
            Map<String, Object> requestData =
                    objectMapper.readValue(
                            request.getData(), new TypeReference<Map<String, Object>>() {});
            FabricTransactionRequest fabricTransactionRequest =
                    new FabricTransactionRequest(
                            (String) requestData.get("sdkConfig"),
                            (String) requestData.get("chaincodeName"),
                            (String) requestData.get("method"),
                            (Object[]) requestData.get("args"));

            com.webank.wecross.stub.fabric2.rpc.methods.Response response;
            if (isEvaluate) {
                response = fabricPRCRest.call(fabricTransactionRequest).send();
            } else {
                response = fabricPRCRest.sendTransaction(fabricTransactionRequest).send();
            }

            if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                return FabricConnectionResponse.build()
                        .errorCode(response.getErrorCode())
                        .errorMessage(response.getMessage());
            } else {
                return FabricConnectionResponse.build()
                        .errorCode(FabricType.TransactionResponseStatus.SUCCESS)
                        .errorMessage(response.getMessage())
                        .data(objectMapper.writeValueAsBytes(response.getData()));
            }
        } catch (Exception e) {
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.INTERNAL_ERROR)
                    .errorMessage(e.getMessage());
        }
    }

    private Response handleGetBlock(Request request) {
        try {
            Map<String, Object> requestData =
                    objectMapper.readValue(
                            request.getData(), new TypeReference<Map<String, Object>>() {});
            GetBlockRequest getBlockRequest =
                    new GetBlockRequest(
                            (String) requestData.get("sdkConfig"),
                            (long) requestData.get("blockNumber"),
                            (boolean) requestData.get("onlyHeader"));
            com.webank.wecross.stub.fabric2.rpc.methods.Response response =
                    fabricPRCRest.getBlock(getBlockRequest).send();
            if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                return FabricConnectionResponse.build()
                        .errorCode(response.getErrorCode())
                        .errorMessage(response.getMessage());
            }
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.SUCCESS)
                    .data(objectMapper.writeValueAsBytes(response.getData()));
        } catch (Exception e) {
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.INTERNAL_ERROR)
                    .errorMessage(e.getMessage());
        }
    }

    private Response handleGetTransaction(Request request) {
        try {
            Map<String, Object> requestData =
                    objectMapper.readValue(
                            request.getData(), new TypeReference<Map<String, Object>>() {});
            GetTransactionRequest transactionRequest =
                    new GetTransactionRequest(
                            (String) requestData.get("sdkConfig"),
                            (String) requestData.get("transactionHash"),
                            (long) requestData.get("blockNumber"),
                            (boolean) requestData.get("isVerified"));
            com.webank.wecross.stub.fabric2.rpc.methods.Response response =
                    fabricPRCRest.getTransactionInfo(transactionRequest).send();
            if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                return FabricConnectionResponse.build()
                        .errorCode(response.getErrorCode())
                        .errorMessage(response.getMessage());
            } else {
                return FabricConnectionResponse.build()
                        .errorCode(FabricType.TransactionResponseStatus.SUCCESS)
                        .errorMessage(response.getMessage())
                        .data(objectMapper.writeValueAsBytes(response.getData()));
            }
        } catch (Exception e) {
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.INTERNAL_ERROR)
                    .errorMessage(e.getMessage());
        }
    }

    private Response handleSubscribeContractEvent(Request request) {
        try {
            Map<String, Object> requestData =
                    objectMapper.readValue(
                            request.getData(), new TypeReference<Map<String, Object>>() {});
            SubscribeEventRequest subscribeEventRequest =
                    new SubscribeEventRequest(
                            (String) requestData.get("sdkConfig"),
                            (String) requestData.get("chaincodeName"),
                            (String) requestData.get("topic"),
                            (long) requestData.get("fromBlock"),
                            (long) requestData.get("endBlock"));
            com.webank.wecross.stub.fabric2.rpc.methods.Response response =
                    fabricPRCRest.subscribeContractEvent(subscribeEventRequest).send();
            if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                return FabricConnectionResponse.build()
                        .errorCode(response.getErrorCode())
                        .errorMessage(response.getMessage());
            } else {
                return FabricConnectionResponse.build()
                        .errorCode(FabricType.TransactionResponseStatus.SUCCESS)
                        .errorMessage(response.getMessage())
                        .data(objectMapper.writeValueAsBytes(response.getData()));
            }
        } catch (Exception e) {
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.INTERNAL_ERROR)
                    .errorMessage(e.getMessage());
        }
    }

    private Response handleUnSubscribeContractEvent(Request request) {
        try {
            Map<String, Object> requestData =
                    objectMapper.readValue(
                            request.getData(), new TypeReference<Map<String, Object>>() {});

            UnSubscribeEventRequest unSubscribeEventRequest =
                    new UnSubscribeEventRequest(
                            (String) requestData.get("sdkConfig"),
                            (String) requestData.get("subscribeEventId"));

            com.webank.wecross.stub.fabric2.rpc.methods.Response response =
                    fabricPRCRest.unSubscribeContractEvent(unSubscribeEventRequest).send();
            if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                return FabricConnectionResponse.build()
                        .errorCode(response.getErrorCode())
                        .errorMessage(response.getMessage());
            } else {
                return FabricConnectionResponse.build()
                        .errorCode(FabricType.TransactionResponseStatus.SUCCESS)
                        .errorMessage(response.getMessage())
                        .data(objectMapper.writeValueAsBytes(response.getData()));
            }
        } catch (Exception e) {
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.INTERNAL_ERROR)
                    .errorMessage(e.getMessage());
        }
    }

    private Response handleRegisterExistingContract(Request request) {
        try {
            String sdkConfig = new String(request.getData(), StandardCharsets.UTF_8);
            GetContractInfoRequest contractInfoRequest =
                    new GetContractInfoRequest(sdkConfig, request.getResourceInfo().getName());
            com.webank.wecross.stub.fabric2.rpc.methods.Response response =
                    fabricPRCRest.getContractInfo(contractInfoRequest).send();
            if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                return FabricConnectionResponse.build()
                        .errorCode(response.getErrorCode())
                        .errorMessage(response.getMessage());
            } else {
                Map<Object, Object> resourceProperties = new HashMap<>();
                resourceProperties.put("CHANNEL_ID", getChannelId());
                resourceProperties.put("CONTRACT_NAME", request.getResourceInfo().getName());
                resourceProperties.put("CONTRACT_ADDRESS", "");
                resourceProperties.put("CONTRACT_VERSION", "");
                resourceProperties.put("CONTRACT_RUNTIME_TYPE", "GO");
                request.getResourceInfo().setProperties(resourceProperties);
                connectionEventHandler.onANewResource(request.getResourceInfo());

                return FabricConnectionResponse.build()
                        .errorCode(FabricType.TransactionResponseStatus.SUCCESS)
                        .errorMessage(response.getMessage())
                        .data(objectMapper.writeValueAsBytes(resourceProperties));
            }
        } catch (Exception e) {
            return FabricConnectionResponse.build()
                    .errorCode(FabricType.TransactionResponseStatus.INTERNAL_ERROR)
                    .errorMessage(e.getMessage());
        }
    }

    @Override
    public void setConnectionEventHandler(ConnectionEventHandler eventHandler) {
        this.connectionEventHandler = eventHandler;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    public List<ResourceInfo> getResources() {
        List<ResourceInfo> resourceInfos = new ArrayList<>();
        try {
            ContractsResponse contractsResponse =
                    fabricPRCRest.getContractList(getChainName(), getChannelId()).send();
            Contracts contracts = contractsResponse.getContracts();

            for (ContractInfo contractInfo : contracts.getContractInfos()) {
                ResourceInfo resourceInfo = new ResourceInfo();
                resourceInfo.setName(contractInfo.getName());
                resourceInfo.setStubType(getStubType());
                resourceInfos.add(resourceInfo);
            }
        } catch (Exception e) {
            logger.error("获取合约列表失败。{}", e.getMessage());
        }

        return resourceInfos;
    }

    public boolean hasProxyDeployed2AllPeers() {
        return true;
    }
}
