package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.*;
import com.webank.wecross.stub.fabric2.account.FabricAccount;
import com.webank.wecross.stub.fabric2.common.FabricType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricDriver implements Driver {
    private Logger logger = LoggerFactory.getLogger(FabricDriver.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ImmutablePair<Boolean, TransactionRequest> decodeTransactionRequest(Request request) {
        return null;
    }

    @Override
    public List<ResourceInfo> getResources(Connection connection) {
        if (connection instanceof FabricConnection) {
            return ((FabricConnection) connection).getResources();
        }

        logger.warn(" Not fabric connection, name: {}", connection.getClass().getName());
        return new ArrayList<>();
    }

    private void asyncSendTransaction(
            TransactionContext context,
            TransactionRequest request,
            Connection connection,
            Callback callback,
            boolean isEvaluate) {
        try {
            FabricAccount account = (FabricAccount) context.getAccount();
            String sdkConfig =
                    FabricSDKConfigGenerator.getOrUpdateSDKConfig(
                            connection.getProperties().get(FabricType.Properties.STUB_PATH),
                            account);
            Map<String, Object> data = new HashMap<>();
            data.put("sdkConfig", sdkConfig);
            data.put("chaincodeName", context.getPath().getResource());
            data.put("method", request.getMethod());
            data.put("args", request.getArgs());
            Request callRequest =
                    Request.newRequest(
                            isEvaluate
                                    ? FabricType.ConnectionMessage.FABRIC_CALL
                                    : FabricType.ConnectionMessage.FABRIC_SENDTRANSACTION,
                            objectMapper.writeValueAsBytes(data));
            connection.asyncSend(
                    callRequest,
                    response -> {
                        if (response.getErrorCode()
                                != FabricType.TransactionResponseStatus.SUCCESS) {
                            logger.error(
                                    "发送交易请求, 响应失败. 请求: {}:{} {}, 失败: {}",
                                    context.getPath().getResource(),
                                    request.getMethod(),
                                    request.getArgs(),
                                    response.getErrorMessage());
                            callback.onTransactionResponse(
                                    new TransactionException(
                                            response.getErrorCode(), response.getErrorMessage()),
                                    null);
                            return;
                        }
                        TransactionResponse transactionResponse = new TransactionResponse();
                        transactionResponse.setErrorCode(
                                FabricType.TransactionResponseStatus.SUCCESS);
                        transactionResponse.setMessage(transactionResponse.getMessage());
                        transactionResponse.setResult(
                                new String[] {new String(response.getData())});
                        callback.onTransactionResponse(null, transactionResponse);
                    });
        } catch (JsonProcessingException e) {
            logger.error(
                    "发送交易请求过程中出现异常. 请求: {}:{} {}, 失败: {}",
                    context.getPath().getResource(),
                    request.getMethod(),
                    request.getArgs(),
                    e);
            callback.onTransactionResponse(
                    new TransactionException(
                            FabricType.TransactionResponseStatus.INTERNAL_ERROR, "格式数据失败"),
                    null);
        } catch (Exception e) {
            logger.error(
                    "发送交易请求过程中出现异常. 请求: {}:{} {}, 失败: {}",
                    context.getPath().getResource(),
                    request.getMethod(),
                    request.getArgs(),
                    e);
            callback.onTransactionResponse(
                    new TransactionException(
                            FabricType.TransactionResponseStatus.INTERNAL_ERROR, "其他错误"),
                    null);
        }
    }

    @Override
    public void asyncCall(
            TransactionContext context,
            TransactionRequest request,
            boolean byProxy,
            Connection connection,
            Callback callback) {
        asyncSendTransaction(context, request, connection, callback, true);
    }

    @Override
    public void asyncSendTransaction(
            TransactionContext context,
            TransactionRequest request,
            boolean byProxy,
            Connection connection,
            Callback callback) {
        asyncSendTransaction(context, request, connection, callback, false);
    }

    @Override
    public void asyncGetBlockNumber(Connection connection, GetBlockNumberCallback callback) {
        asyncGetBlock(
                -1, // 获取最新的区块
                true,
                connection,
                (exception, block) -> {
                    if (exception != null) {
                        callback.onResponse(exception, 0);
                    } else {
                        callback.onResponse(null, block.getBlockHeader().getNumber());
                    }
                });
    }

    @Override
    public void asyncGetBlock(
            long blockNumber,
            boolean onlyHeader,
            Connection connection,
            GetBlockCallback callback) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            String sdkConfig =
                    FabricSDKConfigGenerator.getDefaultSDKConfig(
                            connection.getProperties().get(FabricType.Properties.STUB_PATH));
            requestData.put("sdkConfig", sdkConfig);
            requestData.put("blockNumber", blockNumber);
            requestData.put("onlyHeader", onlyHeader);
            Request request =
                    Request.newRequest(
                            FabricType.ConnectionMessage.FABRIC_GET_BLOCK,
                            objectMapper.writeValueAsBytes(requestData));
            connection.asyncSend(
                    request,
                    response -> {
                        if (response.getErrorCode()
                                != FabricType.TransactionResponseStatus.SUCCESS) {
                            callback.onResponse(
                                    new Exception(String.format("%s", response.getErrorMessage())),
                                    null);
                            return;
                        }

                        try {
                            Map<String, Object> responseData =
                                    objectMapper.readValue(
                                            response.getData(),
                                            new TypeReference<Map<String, Object>>() {});

                            int blockHeight = (int) responseData.get("blockNumber");
                            Block block = new Block();
                            BlockHeader blockHeader = new BlockHeader();
                            blockHeader.setNumber((long) blockHeight);
                            blockHeader.setHash((String) responseData.get("dataHash"));
                            blockHeader.setPrevHash((String) responseData.get("previousHash"));
                            block.setBlockHeader(blockHeader);
                            block.setTransactionsHashes(null);
                            block.setRawBytes(response.getData());
                            callback.onResponse(null, block);
                        } catch (IOException e) {
                            callback.onResponse(e, null);
                        }
                    });
        } catch (Exception e) {
            logger.error("asyncGetBlock was failure. {}", e.getMessage());
            callback.onResponse(
                    new Exception(
                            String.format(
                                    "%d", FabricType.TransactionResponseStatus.INTERNAL_ERROR)),
                    null);
        }
    }

    @Override
    public void asyncGetTransaction(
            String transactionHash,
            long blockNumber,
            BlockManager blockManager,
            boolean isVerified,
            Connection connection,
            GetTransactionCallback callback) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            String sdkConfig =
                    FabricSDKConfigGenerator.getDefaultSDKConfig(
                            connection.getProperties().get(FabricType.Properties.STUB_PATH));
            requestData.put("sdkConfig", sdkConfig);
            requestData.put("transactionHash", transactionHash);
            requestData.put("blockNumber", blockNumber);
            requestData.put("isVerified", isVerified);
            Request request =
                    Request.newRequest(
                            FabricType.ConnectionMessage.FABRIC_GET_TRANSACTION,
                            objectMapper.writeValueAsBytes(requestData));
            connection.asyncSend(
                    request,
                    response -> {
                        if (response.getErrorCode()
                                != FabricType.TransactionResponseStatus.SUCCESS) {
                            callback.onResponse(
                                    new Exception(String.format("%s", response.getErrorMessage())),
                                    null);
                        } else {
                            Transaction transaction = new Transaction();
                            transaction.setReceiptBytes(response.getData());
                            callback.onResponse(null, transaction);
                        }
                    });

        } catch (Exception e) {
            logger.error("asyncGetTransaction was failure. {}", e);
            callback.onResponse(
                    new Exception(
                            String.format(
                                    "%d", FabricType.TransactionResponseStatus.INTERNAL_ERROR)),
                    null);
        }
    }

    @Override
    public void subscribeEvent(
            TransactionContext context,
            SubscribeRequest request,
            Connection connection,
            Driver.Callback callback) {
        try {
            FabricAccount account = (FabricAccount) context.getAccount();
            String sdkConfig =
                    FabricSDKConfigGenerator.getOrUpdateSDKConfig(
                            connection.getProperties().get(FabricType.Properties.STUB_PATH),
                            account);
            String topic = request.getTopics().get(0).trim();
            Request connectionRequest;
            Map<String, Object> requestData = new HashMap<>();
            if ("@cancel".equals(topic)) {
                requestData.put("sdkConfig", sdkConfig);
                // TODO
                // Fabric2 api service 维护 subscribeEventId
                requestData.put("subscribeEventId", request.getTopics().get(1));
                connectionRequest =
                        Request.newRequest(
                                FabricType.ConnectionMessage.FABRIC_UNSUBSCRIBE_CONTRACT,
                                objectMapper.writeValueAsBytes(requestData));
            } else {
                requestData.put("sdkConfig", sdkConfig);
                requestData.put("chaincodeName", context.getPath().getResource());
                requestData.put("topic", topic);
                requestData.put("fromBlock", request.getFromBlockNumber());
                requestData.put("endBlock", request.getToBlockNumber());
                connectionRequest =
                        Request.newRequest(
                                FabricType.ConnectionMessage.FABRIC_SUBSCRIBE_CONTRACT,
                                objectMapper.writeValueAsBytes(requestData));
            }

            connection.asyncSend(
                    connectionRequest,
                    response -> {
                        if (response.getErrorCode()
                                != FabricType.TransactionResponseStatus.SUCCESS) {
                            callback.onTransactionResponse(
                                    new TransactionException(
                                            response.getErrorCode(), response.getErrorMessage()),
                                    null);
                            return;
                        }
                        TransactionResponse transactionResponse = new TransactionResponse();
                        if (connectionRequest.getType()
                                == FabricType.ConnectionMessage.FABRIC_SUBSCRIBE_CONTRACT) {
                            String handle = new String(response.getData(), StandardCharsets.UTF_8);
                            transactionResponse.setMessage(handle);
                            List<String> result = new ArrayList<>();
                            result.add(String.format("path:%s", context.getPath()));
                            result.add(String.format("topics:%s", topic));
                            result.add(String.format("raw topics:%s", request.getTopics().get(0)));
                            result.add(String.format("from:%d", request.getFromBlockNumber()));
                            result.add(String.format("to:%d", request.getToBlockNumber()));
                            transactionResponse.setResult(result.stream().toArray(String[]::new));
                        } else {
                            String handler = request.getTopics().get(1);
                            transactionResponse.setMessage(String.format("订阅事件取消成功。%s", handler));
                        }
                        callback.onTransactionResponse(null, transactionResponse);
                    });

        } catch (Exception e) {
            logger.error("subscribeEvent was failure. {}", e);
            callback.onTransactionResponse(
                    new TransactionException(
                            FabricType.TransactionResponseStatus.INTERNAL_ERROR, e.getMessage()),
                    null);
        }
    }

    @Override
    public void asyncCustomCommand(
            String command,
            Path path,
            Object[] args,
            Account account,
            BlockManager blockManager,
            Connection connection,
            CustomCommandCallback callback) {
        try {
            switch (command) {
                case "REGISTER_EXISTING_CONTRACT":
                    registerExistingContract(
                            path, args, account, blockManager, connection, callback);
                    break;
                default:
                    callback.onResponse(
                            new Exception(String.format("不支持该自定义命令: %s", command)), null);
            }
        } catch (Exception e) {
            callback.onResponse(new Exception(String.format("处理 %s 异常", command)), null);
        }
    }

    private void registerExistingContract(
            Path path,
            Object[] args,
            Account account,
            BlockManager blockManager,
            Connection connection,
            CustomCommandCallback callback)
            throws Exception {
        FabricAccount fabricAccount = (FabricAccount) account;
        String sdkConfig =
                FabricSDKConfigGenerator.getOrUpdateSDKConfig(
                        connection.getProperties().get(FabricType.Properties.STUB_PATH),
                        fabricAccount);
        Request request =
                Request.newRequest(
                        FabricType.ConnectionMessage.FABRIC_REGISTER_EXISTING_CONTRACT, sdkConfig);
        request.setPath(path.toString());

        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setStubType(connection.getProperties().get(FabricType.Properties.STUB_TYPE));
        resourceInfo.setName(path.getResource());
        resourceInfo.getProperties().put("mspId", fabricAccount.getMspID());
        resourceInfo.getProperties().put("name", fabricAccount.getName());
        resourceInfo.getProperties().put("orgName", fabricAccount.getOrgName());

        request.setResourceInfo(resourceInfo);
        connection.asyncSend(
                request,
                response -> {
                    if (response.getErrorCode() != FabricType.TransactionResponseStatus.SUCCESS) {
                        callback.onResponse(
                                new Exception(
                                        String.format(
                                                "注册合约 %s 失败。 %s",
                                                path.getResource(), response.getErrorMessage())),
                                null);
                    } else {
                        try {
                            Map<String, Object> data =
                                    objectMapper.readValue(
                                            response.getData(),
                                            new TypeReference<Map<String, Object>>() {});
                            callback.onResponse(null, data);
                        } catch (Exception e) {
                            callback.onResponse(new Exception(String.format("处理结果失败")), null);
                        }
                    }
                });
    }

    @Override
    public byte[] accountSign(Account account, byte[] message) {
        return null;
    }

    @Override
    public boolean accountVerify(String identity, byte[] signBytes, byte[] message) {
        return false;
    }
}
