package com.webank.wecross.stub.fabric2.rpc;

import com.webank.wecross.stub.fabric2.rpc.methods.Response;

public interface FabricRPC {
    RemoteCall<Response> InitConfiguration(String configuration) throws Exception;

    RemoteCall<Response> instantiateRemoteService() throws Exception;
}
