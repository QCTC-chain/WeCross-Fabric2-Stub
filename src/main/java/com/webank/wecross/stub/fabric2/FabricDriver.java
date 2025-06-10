package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.*;
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

    @Override
    public void asyncCall(
            TransactionContext context,
            TransactionRequest request,
            boolean byProxy,
            Connection connection,
            Callback callback) {}

    @Override
    public void asyncSendTransaction(
            TransactionContext context,
            TransactionRequest request,
            boolean byProxy,
            Connection connection,
            Callback callback) {}

    @Override
    public void asyncGetBlockNumber(Connection connection, GetBlockNumberCallback callback) {}

    @Override
    public void asyncGetBlock(
            long blockNumber,
            boolean onlyHeader,
            Connection connection,
            GetBlockCallback callback) {}

    @Override
    public void asyncGetTransaction(
            String transactionHash,
            long blockNumber,
            BlockManager blockManager,
            boolean isVerified,
            Connection connection,
            GetTransactionCallback callback) {}

    @Override
    public void subscribeEvent(
            TransactionContext context,
            SubscribeRequest request,
            Connection connection,
            Driver.Callback callback) {}

    @Override
    public void asyncCustomCommand(
            String command,
            Path path,
            Object[] args,
            Account account,
            BlockManager blockManager,
            Connection connection,
            CustomCommandCallback callback) {}

    @Override
    public byte[] accountSign(Account account, byte[] message) {
        return null;
    }

    @Override
    public boolean accountVerify(String identity, byte[] signBytes, byte[] message) {
        return false;
    }
}
