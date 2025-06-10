package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.Connection;
import com.webank.wecross.stub.Request;
import com.webank.wecross.stub.ResourceInfo;
import com.webank.wecross.stub.Response;
import com.webank.wecross.stub.fabric2.rpc.FabricPRCRest;
import com.webank.wecross.stub.fabric2.rpc.service.FabricRPCService;
import com.webank.wecross.stub.fabric2.rpc.service.FabricService;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** @Description: fabric 链接对象，用于链接fabric链 发起交易 @Author: mirsu @Date: 2020/10/30 10:52 */
public class FabricConnection implements Connection {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private FabricPRCRest fabricPRCRest;
    private Logger logger = LoggerFactory.getLogger(FabricConnection.class);
    private ThreadPoolTaskExecutor threadPool;
    private Map<String, String> properties = new HashMap<>();

    public FabricConnection(ThreadPoolTaskExecutor threadPool) {
        this.threadPool = threadPool;
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
    }

    private Response send(Request request) {
        return null;
    }

    @Override
    public void asyncSend(Request request, Connection.Callback callback) {}

    @Override
    public void setConnectionEventHandler(ConnectionEventHandler eventHandler) {}

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    public List<ResourceInfo> getResources() {
        return null;
    }

    public boolean hasProxyDeployed2AllPeers() {
        return true;
    }
}
