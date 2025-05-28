package com.webank.wecross.stub.fabric;

import com.webank.wecross.stub.fabric2.FabricStubConfigParser;
import com.webank.wecross.stub.fabric2.FabricStubFactory;
import org.junit.Assert;
import org.junit.Test;

public class FabricStubConfigParserTest {
    @Test
    public void loadTest() throws Exception {
        FabricStubConfigParser parser = new FabricStubConfigParser("classpath:chains/fabric2/");
        Assert.assertTrue(parser != null);
        Assert.assertTrue(parser.getCommon() != null);
        Assert.assertTrue(parser.getFabricServices() != null);
        Assert.assertTrue(parser.getOrgs() != null);
        Assert.assertTrue(parser.getAdvanced() != null);
    }

    @Test
    public void parseStubConfig() throws Exception {
        String chainType = "Fabric2.0";
        String chainName = "fabric";
        String stubConfig =
                "{\n"
                        + "  \"fabricServices\": {\n"
                        + "    \"channelName\": \"mychannel\",\n"
                        + "    \"orgUserName\": \"fabric2_admin_org1\",\n"
                        + "    \"ordererTlsCaFile\": \"ossId1\",\n"
                        + "    \"ordererAddress\": [\n"
                        + "      \"grpcs://localhost:7050\"\n"
                        + "    ]\n"
                        + "  },\n"
                        + "  \"orgs\": [\n"
                        + "    {\n"
                        + "      \"id\": \"org1\",\n"
                        + "      \"name\": \"org1\",\n"
                        + "      \"tlsCaFile\": \"ossId2\",\n"
                        + "      \"admin\": {\n"
                        + "        \"name\": \"fabric2_admin_org1\",\n"
                        + "        \"mspid\": \"Org1MSP\",\n"
                        + "        \"crtFile\": \"ossid01\",\n"
                        + "        \"keyFile\": \"ossid02\"\n"
                        + "      },\n"
                        + "      \"endorsers\": [\n"
                        + "        \"grpcs://localhost:7051\"\n"
                        + "      ]\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}";
        String[] args = new String[] {chainType, chainName, stubConfig};
        FabricStubFactory factory = new FabricStubFactory();
        factory.generateConnection("/Users/dbliu/Desktop/Desktop/WeCross/test-fabric2", args);
    }
}
