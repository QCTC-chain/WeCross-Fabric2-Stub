package com.webank.wecross.stub.fabric2.rpc;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.methods.request.*;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractsResponse;

public interface FabricRPC {
    RemoteCall<Response> initConfiguration(String configuration);

    RemoteCall<Response> instantiateRemoteService();

    RemoteCall<Response> getBlock(GetBlockRequest request);

    RemoteCall<ContractsResponse> getContractList(String chainName, String channelId);

    RemoteCall<Response> getContractInfo(GetContractInfoRequest contractInfoRequest);

    RemoteCall<Response> subscribeContractEvent(SubscribeEventRequest subscribeEventRequest);

    RemoteCall<Response> unSubscribeContractEvent(UnSubscribeEventRequest unSubscribeEventRequest);

    RemoteCall<Response> call(FabricTransactionRequest transactionRequest);

    RemoteCall<Response> sendTransaction(FabricTransactionRequest transactionRequest);

    RemoteCall<Response> getTransactionInfo(GetTransactionRequest transactionRequest);
}
