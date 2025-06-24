package com.webank.wecross.stub.fabric2;

import com.webank.wecross.stub.fabric2.account.FabricAccount;
import com.webank.wecross.stub.fabric2.account.FabricAccountFactory;
import org.junit.Test;

public class StubConfigGeneratorTest {
    private final String stubConfigStr =
            "{\n"
                    + "\"channelName\": \"mychannel\",\n"
                    + "  \"user\": {\n"
                    + "    \"orgName\": \"Org1\",\n"
                    + "    \"mspId\": \"Org1MSP\",\n"
                    + "    \"name\": \"admin\",\n"
                    + "    \"crt\": \"/Users/dbliu/Desktop/accounts/admin/account.crt\",\n"
                    + "    \"key\": \"/Users/dbliu/Desktop/accounts/admin/account.key\"\n"
                    + "  },\n"
                    + "  \"orders\": [\n"
                    + "    {\n"
                    + "      \"domain\": \"orderer.example.com\",\n"
                    + "      \"tlsCa\": \"/Users/dbliu/Desktop/order-cert/orderer.example.com/orderer-tlsca.crt\",\n"
                    + "      \"address\": \"grpcs://192.168.11.38:7050\"\n"
                    + "    }\n"
                    + "  ],\n"
                    + "  \"peers\": [\n"
                    + "    {\n"
                    + "      \"orgName\": \"Org1\",\n"
                    + "      \"domain\": \"peer0.org1.example.com\",\n"
                    + "      \"tlsCa\": \"/Users/dbliu/Desktop/peers-pem/peer0.org1.example.com-cert.crt\",\n"
                    + "      \"address\": \"grpcs://192.168.11.38:7051\"\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}";

    private final String mqConfigStr =
            "{\n"
                    + "  \"mq\": {\n"
                    + "    \"type\": \"rocketmq\",\n"
                    + "    \"host\": \"192.168.1.45\",\n"
                    + "    \"port\": 8081,\n"
                    + "    \"userName\": \"null\",\n"
                    + "    \"password\": \"null\",\n"
                    + "    \"topic\": \"wecross\",\n"
                    + "    \"group\": \"\"\n"
                    + "  }\n"
                    + "}";

    @Test
    public void generateStubConfigTest() {
        try {
            String sdkConfig = FabricSDKConfigGenerator.generateSDKConfig(stubConfigStr);
            System.out.println(sdkConfig);

            sdkConfig =
                    FabricSDKConfigGenerator.generateSDKConfigFrom(
                            "file:///Users/dbliu/Desktop/stub.toml");
            System.out.println(sdkConfig);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void updateSDKConfigTest() {
        try {
            FabricAccount account =
                    FabricAccountFactory.build(
                            "fabric2_admin2", "Org2", "Org2MSP", "pubkey1", "secKey1");
            String sdkConfig =
                    FabricSDKConfigGenerator.getOrUpdateSDKConfig(
                            "file:///Users/dbliu/Desktop/", account);
            System.out.println(sdkConfig);
        } catch (Exception e) {

        }
    }

    @Test
    public void fabricStubFactoryTest() {
        FabricStubBaseFactory factory = new FabricStubBaseFactory();
        String[] args = new String[] {"GM_Fabric2.0", "Fabric2", stubConfigStr, mqConfigStr};
        factory.generateConnection("/Users/dbliu/Desktop/Fabric2", args);
    }
}
