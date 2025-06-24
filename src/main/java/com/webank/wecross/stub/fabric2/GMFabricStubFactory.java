package com.webank.wecross.stub.fabric2;

import com.webank.wecross.stub.Stub;
import com.webank.wecross.stub.fabric2.common.FabricType;

@Stub(FabricType.GM_STUB_NAME)
public class GMFabricStubFactory extends FabricStubBaseFactory {
    public GMFabricStubFactory() {
        super(FabricType.GM_STUB_NAME);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(
                "This is "
                        + FabricType.GM_STUB_NAME
                        + " Stub Plugin. Please copy this file to router/plugin/");
        System.out.println("To deploy WeCrossProxy:");
        System.out.println(
                "    java -cp conf/:lib/*:plugin/* com.webank.wecross.stub.fabric2.proxy.ProxyChaincodeDeployment ");
        System.out.println("To performance test, please run the command for more info:");
    }
}
