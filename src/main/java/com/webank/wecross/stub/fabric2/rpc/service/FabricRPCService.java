package com.webank.wecross.stub.fabric2.rpc.service;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.fabric2.exception.ErrorCode;
import com.webank.wecross.stub.fabric2.exception.FabricRPCException;
import com.webank.wecross.stub.fabric2.rpc.methods.Callback;
import com.webank.wecross.stub.fabric2.rpc.methods.Request;
import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricRPCService implements FabricService {

    private final Logger logger = LoggerFactory.getLogger(FabricRPCService.class);
    private static final int HTTP_CLIENT_TIME_OUT = 100000; // ms
    private String server;
    private AsyncHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private AsyncHttpClient getHttpAsyncClient() throws FabricRPCException {
        try {
            DefaultAsyncHttpClientConfig.Builder builder = config();
            builder.setConnectTimeout(HTTP_CLIENT_TIME_OUT)
                    .setRequestTimeout(HTTP_CLIENT_TIME_OUT)
                    .setReadTimeout(HTTP_CLIENT_TIME_OUT)
                    .setHandshakeTimeout(HTTP_CLIENT_TIME_OUT)
                    .setShutdownTimeout(HTTP_CLIENT_TIME_OUT)
                    .setPooledConnectionIdleTimeout(HTTP_CLIENT_TIME_OUT)
                    .setAcquireFreeChannelTimeout(HTTP_CLIENT_TIME_OUT)
                    .setConnectionPoolCleanerPeriod(HTTP_CLIENT_TIME_OUT)
                    .setKeepAlive(true);
            return asyncHttpClient(builder);
        } catch (Exception e) {
            logger.error("Init http client error: ", e);
            throw new FabricRPCException(
                    ErrorCode.INTERNAL_ERROR, "Init http client error: " + e.getMessage());
        }
    }

    @Override
    public void init(String server) throws FabricRPCException {
        if (server != null && !server.isEmpty()) {
            this.server = server;
        } else {
            this.server = "http://127.0.0.1:9090";
        }

        logger.info("RPC service init: {}", server);
        httpClient = getHttpAsyncClient();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void checkRequest(Request<?> request) throws FabricRPCException {
        if (request.getVersion().isEmpty()) {
            throw new FabricRPCException(ErrorCode.RPC_ERROR, "Request version is empty");
        }
    }

    @Override
    public <T extends Response> T send(
            String httpMethod, String uri, Request request, Class<T> responseType)
            throws FabricRPCException {
        checkRequest(request);
        CompletableFuture<T> responseFuture = new CompletableFuture<>();
        CompletableFuture<FabricRPCException> exceptionFuture = new CompletableFuture<>();
        asyncSend(
                httpMethod,
                uri,
                request,
                responseType,
                new Callback<T>() {
                    @Override
                    public void onSuccess(T response) {
                        responseFuture.complete(response);
                        exceptionFuture.complete(null);
                    }

                    @Override
                    public void onFailed(FabricRPCException e) {
                        logger.warn("send onFailed: ", e);
                        responseFuture.complete(null);
                        exceptionFuture.complete(e);
                    }
                });

        try {
            T response = responseFuture.get(20, TimeUnit.SECONDS);
            FabricRPCException exception = exceptionFuture.get(20, TimeUnit.SECONDS);

            if (logger.isDebugEnabled()) {
                logger.debug("response: {}", response);
            }

            if (exception != null) {
                throw exception;
            }

            return response;
        } catch (TimeoutException e) {
            logger.warn("http request timeout");
            throw new FabricRPCException(
                    ErrorCode.RPC_ERROR, "http request timeout, caused by: " + e.getMessage());
        } catch (Exception e) {
            logger.error("e: ", e);
            throw new FabricRPCException(
                    ErrorCode.RPC_ERROR, "http request failed, caused by: " + e.getMessage());
        }
    }

    @Override
    public <T extends Response> void asyncSend(
            String httpMethod,
            String uri,
            Request<?> request,
            Class<T> responseType,
            Callback<T> callback) {
        try {
            String url = server + uri;
            if (logger.isDebugEnabled()) {
                logger.debug("request: {}; url: {}", objectMapper.writeValueAsString(request), url);
            }

            checkRequest(request);
            BoundRequestBuilder builder = httpClient.prepare(httpMethod.toUpperCase(), url);

            builder.setHeader("Accept", "application/json")
                    .setHeader("Content-Type", "application/json")
                    .setBody(objectMapper.writeValueAsString(request.getData()))
                    .execute(
                            new AsyncCompletionHandler<Object>() {
                                @Override
                                public Object onCompleted(org.asynchttpclient.Response httpResponse)
                                        throws Exception {
                                    try {
                                        if (httpResponse.getStatusCode() == 401) {
                                            callback.callOnFailed(
                                                    new FabricRPCException(
                                                            ErrorCode.LACK_AUTHENTICATION,
                                                            "HTTP status code: 401-Unauthorized, have you logged in?\n"
                                                                    + "If you have logged-in already, maybe you should re-login "
                                                                    + "because your account login status has expired."));
                                            return null;
                                        }
                                        if (httpResponse.getStatusCode() == 404) {
                                            callback.callOnFailed(
                                                    new FabricRPCException(
                                                            ErrorCode.LACK_AUTHENTICATION,
                                                            "HTTP status code: 404 Not Found\n"
                                                                    + "Maybe your request's resource path is wrong."));
                                            return null;
                                        }
                                        if (httpResponse.getStatusCode() != 200) {
                                            callback.callOnFailed(
                                                    new FabricRPCException(
                                                            ErrorCode.RPC_ERROR,
                                                            "HTTP response status: "
                                                                    + httpResponse.getStatusCode()
                                                                    + " message: "
                                                                    + httpResponse
                                                                            .getStatusText()));
                                            return null;
                                        } else {
                                            String content = httpResponse.getResponseBody();
                                            T response =
                                                    objectMapper.readValue(content, responseType);
                                            callback.callOnSuccess(response);
                                            return response;
                                        }
                                    } catch (Exception e) {
                                        callback.callOnFailed(
                                                new FabricRPCException(
                                                        ErrorCode.INTERNAL_ERROR,
                                                        "handle response failed: " + e.toString()));
                                        return null;
                                    }
                                }

                                @Override
                                public void onThrowable(Throwable t) {
                                    callback.callOnFailed(
                                            new FabricRPCException(
                                                    ErrorCode.RPC_ERROR,
                                                    "AsyncSend exception: "
                                                            + t.getCause().toString()));
                                }
                            });

        } catch (FabricRPCException e) {
            logger.error("Catch SDKException in asyncSend, errorMessage: {}", e.getMessage(), e);
            callback.callOnFailed(
                    new FabricRPCException(
                            ErrorCode.INTERNAL_ERROR,
                            "SDKException happened in asyncSend, errorMessage:" + e.getMessage()));
        } catch (Exception e) {
            logger.error("Encode json error when async sending: ", e);
            callback.callOnFailed(
                    new FabricRPCException(
                            ErrorCode.INTERNAL_ERROR,
                            "Encode json error when async sending: " + e.getMessage()));
        }
    }
}
