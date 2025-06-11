package com.webank.wecross.stub.fabric2.common;

public class FabricType {
    public static final String STUB_NAME = "Fabric2.0";

    public static final class Account {
        public static final String FABRIC_ACCOUNT = STUB_NAME;
    }

    public static final class ConnectionMessage {
        // Connection send message type
        public static final int FABRIC_CALL = 2001;
        public static final int FABRIC_SENDTRANSACTION = 2002;
        public static final int FABRIC_GET_BLOCK_NUMBER = 2003;
        public static final int FABRIC_GET_BLOCK = 2004;
        public static final int FABRIC_GET_TRANSACTION = 2005;
        public static final int FABRIC_SUBSCRIBE_CONTRACT = 2006;
        public static final int FABRIC_UNSUBSCRIBE_CONTRACT = 2007;
    }

    public static class TransactionResponseStatus {
        // Chaincode response errorcode
        public static final int SUCCESS = 0;
        public static final int FABRIC_EXECUTE_CHAINCODE_FAILED = 3000;
        public static final int FABRIC_INVOKE_CHAINCODE_FAILED = 3001;
        public static final int FABRIC_COMMIT_CHAINCODE_FAILED = 3002;
        public static final int FABRIC_TX_ONCHAIN_VERIFY_FAIED = 3003;

        public static final int INTERNAL_ERROR = 3101;
        public static final int ILLEGAL_REQUEST_TYPE = 3102;
        public static final int RESOURCE_NOT_FOUND = 3103;
        public static final int FABRIC_REGISTER_CHAINCODE_EVENT_FAILED = 3104;
        public static final int FABRIC_UNREGISTER_CHAINCODE_EVENT_FAILED = 3105;
    }
}
