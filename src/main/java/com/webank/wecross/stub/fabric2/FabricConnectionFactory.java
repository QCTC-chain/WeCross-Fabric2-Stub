package com.webank.wecross.stub.fabric2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class FabricConnectionFactory {
    private static Logger logger = LoggerFactory.getLogger(FabricConnectionFactory.class);

    /**
     * @Description: 构建fabric链接
     *
     * @params: [path]
     * @return: com.webank.wecross.stub.fabric2.FabricConnection @Author: mirsu @Date: 2020/10/30
     *     11:02
     */
    public static FabricConnection build(String path) {
        try {
            ThreadPoolTaskExecutor threadPool = buildThreadPool();
            return new FabricConnection(path, threadPool);
        } catch (Exception e) {
            logger.error("FabricConnection build exception 0: " + e);
            return null;
        }
    }

    private static ThreadPoolTaskExecutor buildThreadPool() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        int corePoolSize = 32;
        int maxPoolSize = 32;
        int queueCapacity = 10000;
        threadPool.setCorePoolSize(corePoolSize);
        threadPool.setMaxPoolSize(maxPoolSize);
        threadPool.setQueueCapacity(queueCapacity);
        threadPool.setThreadNamePrefix("FabricConnection-");
        logger.info(
                "Init threadPool with corePoolSize:{}, maxPoolSize:{}, queueCapacity:{}",
                corePoolSize,
                maxPoolSize,
                queueCapacity);
        return threadPool;
    }
}
