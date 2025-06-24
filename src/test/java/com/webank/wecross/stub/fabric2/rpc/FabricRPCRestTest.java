package com.webank.wecross.stub.fabric2.rpc;

import com.webank.wecross.stub.fabric2.FabricSDKConfigGenerator;
import com.webank.wecross.stub.fabric2.rpc.methods.Response;
import com.webank.wecross.stub.fabric2.rpc.methods.request.GetContractListRequest;
import com.webank.wecross.stub.fabric2.rpc.methods.request.InstantiationRequest;
import com.webank.wecross.stub.fabric2.rpc.methods.response.ContractsResponse;
import com.webank.wecross.stub.fabric2.rpc.service.FabricRPCService;
import org.junit.Test;

public class FabricRPCRestTest {
    @Test
    public void sendRequestTest() {
        try {
            String sdkConfig =
                    FabricSDKConfigGenerator.getDefaultSDKConfig(
                            "file:///Users/dbliu/Desktop/Desktop/WeCross/docker/bmsp-cross/fabric2/conf/chains/mychannel");
            FabricRPCService fabricRPCService = new FabricRPCService();
            fabricRPCService.init();

            InstantiationRequest request = new InstantiationRequest(sdkConfig);

            FabricPRCRest fabricPRCRest = new FabricPRCRest(fabricRPCService);
            Response response = fabricPRCRest.instantiateRemoteService(request).send();
            if (response.getErrorCode() == 0) {
                System.out.println(response.getData());
            } else {
                System.err.println(response.getMessage());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void getContractListTest() {
        try {
            String sdkConfig =
                    FabricSDKConfigGenerator.getDefaultSDKConfig(
                            "file:///Users/dbliu/Desktop/Desktop/WeCross/docker/bmsp-cross/fabric2/conf/chains/mychannel");
            FabricRPCService fabricRPCService = new FabricRPCService();
            fabricRPCService.init();

            GetContractListRequest request = new GetContractListRequest(sdkConfig);
            request.setGM(true);

            FabricPRCRest fabricPRCRest = new FabricPRCRest(fabricRPCService);
            ContractsResponse response = fabricPRCRest.getContractList(request).send();
            if (response.getErrorCode() == 0) {
                System.out.println(response.getData());
            } else {
                System.err.println(response.getMessage());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
