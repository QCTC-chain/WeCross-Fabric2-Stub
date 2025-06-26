package com.webank.wecross.stub.fabric2.rpc;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.methods.request.*;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractResultResponse;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractsResponse;
import com.webank.wecross.stub.fabric2.rpc.methods.response.SubscribeResponse;

public interface FabricRPC {
    RemoteCall<Response> initConfiguration(String configuration);

    RemoteCall<Response> instantiateRemoteService(InstantiationRequest instantiationRequest);

    RemoteCall<Response> getBlock(GetBlockRequest request);

    RemoteCall<ContractsResponse> getContractList(GetContractListRequest contractListRequest);

    RemoteCall<Response> getContractInfo(GetContractInfoRequest contractInfoRequest);

    RemoteCall<SubscribeResponse> subscribeContractEvent(
            SubscribeEventRequest subscribeEventRequest);

    RemoteCall<SubscribeResponse> unSubscribeContractEvent(
            UnSubscribeEventRequest unSubscribeEventRequest);

    RemoteCall<ContractResultResponse> call(FabricTransactionRequest transactionRequest);

    RemoteCall<ContractResultResponse> sendTransaction(FabricTransactionRequest transactionRequest);

    RemoteCall<Response> getTransactionInfo(GetTransactionRequest transactionRequest);
}
