package com.webank.wecross.stub.fabric2.rpc;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.methods.request.FabricTransactionRequest;
import com.webank.wecross.stub.fabric2.rpc.methods.request.SubscribeEventRequest;
import com.webank.wecross.stub.fabric2.rpc.methods.request.UnSubscribeEventRequest;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractsResponse;

public interface FabricRPC {
    RemoteCall<Response> initConfiguration(String configuration);

    RemoteCall<Response> instantiateRemoteService();

    RemoteCall<Response> getBlock(String channelId, long blockNumber, boolean onlyHeader);

    RemoteCall<ContractsResponse> getContractList(String channelId);

    RemoteCall<Response> getContractInfo(String channelId, String chaincodeId);

    RemoteCall<Response> subscribeContractEvent(SubscribeEventRequest subscribeEventRequest);

    RemoteCall<Response> unSubscribeContractEvent(UnSubscribeEventRequest unSubscribeEventRequest);

    RemoteCall<Response> call(FabricTransactionRequest transactionRequest);

    RemoteCall<Response> sendTransaction(FabricTransactionRequest transactionRequest);

    RemoteCall<Response> getTransactionInfo(String channelId, String txId);
}
