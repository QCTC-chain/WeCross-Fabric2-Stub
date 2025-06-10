package com.webank.wecross.stub.fabric2.exception;

public class FabricRPCException extends Exception {
    private static final long serialVersionUID = 3754251347587995515L;

    private final Integer errorCode;

    public FabricRPCException(Integer code, String message) {
        super(message);
        errorCode = code;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
