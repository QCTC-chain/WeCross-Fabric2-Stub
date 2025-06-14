package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.webank.wecross.stub.fabric2.config.StubConfig;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubConfigGenerator {
    private static final String VERSION = "1.0.0";

    private static void writeContent(File file, String content) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.createNewFile()) {
            return;
        }

        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(content);
        } finally {
            fileWriter.close();
        }
    }

    private static Map<String, Object> getChannels(StubConfig stubConfig) {
        /*
        channels:
          mychannel:
            peers:
              peer0.org1.example.com:
                endorsingPeer: true
                chaincodeQuery: true
                ledgerQuery: true
                eventSource: true
        * */
        Map<String, Object> channels = new HashMap<>();
        String channelId = stubConfig.getFabricServices().getChannelName();

        Map<String, Object> channelConfig = new HashMap<>();

        Map<String, Object> onePeerFixedConfig = new HashMap<>();
        onePeerFixedConfig.put("endorsingPeer", true);
        onePeerFixedConfig.put("chaincodeQuery", true);
        onePeerFixedConfig.put("ledgerQuery", true);
        onePeerFixedConfig.put("eventSource", true);
        Map<String, Object> peers = new HashMap<>();
        for (StubConfig.Org org : stubConfig.getOrgs()) {
            int index = 0;
            for (StubConfig.Peer peer : org.getPeers()) {
                String peerId = String.format("peer%d.%s.example.com", index++, org.getName());
                peers.put(peerId, onePeerFixedConfig);
            }
        }
        channelConfig.put("peers", peers);
        channels.put(channelId, channelConfig);
        return channels;
    }

    private static Map<String, Object> getOrders(StubConfig stubConfig, String outputPath)
            throws IOException {
        /*
        orderers:
          orderer.example.com:
            url: 192.168.11.38:7050
            grpcOptions:
              ssl-target-name-override: orderer.example.com
              fail-fast: false
              allow-insecure: false
            tlsCACerts:
              path: /Users/orderer-tlsca.crt
        * */
        Map<String, Object> orders = new HashMap<>();
        String orderId = "orderer.example.com";

        Map<String, Object> orderConfig = new HashMap<>();
        orderConfig.put("url", stubConfig.getFabricServices().getOrdererAddress());

        Map<String, Object> grpcOptions = new HashMap<>();
        grpcOptions.put("ssl-target-name-override", orderId);
        grpcOptions.put("fail-fast", false);
        grpcOptions.put("allow-insecure", false);
        orderConfig.put("grpcOptions", grpcOptions);

        Map<String, Object> tlsCACerts = new HashMap<>();
        File tlsCaFile =
                new File(
                        outputPath
                                + File.separator
                                + "order-cert"
                                + File.separator
                                + "orderer-tlsca.crt");
        writeContent(tlsCaFile, stubConfig.getFabricServices().getOrdererTlsCa());
        tlsCACerts.put("path", tlsCaFile.getAbsolutePath());
        orderConfig.put("tlsCACerts", tlsCACerts);

        orders.put(orderId, orderConfig);
        return orders;
    }

    private static Map<String, Object> getPeers(StubConfig stubConfig, String outputPath)
            throws IOException {
        /*
        * peers:
              peer0.org1.example.com:
                url: 192.168.11.38:7051
                grpcOptions:
                  ssl-target-name-override: peer0.org1.example.com
                  fail-fast: false
                  allow-insecure: false
                tlsCACerts:
                  path: /Users/org1CA/ca.org1.example.com-cert.pem
        */
        Map<String, Object> peers = new HashMap<>();
        for (StubConfig.Org org : stubConfig.getOrgs()) {
            int index = 0;
            for (StubConfig.Peer peer : org.getPeers()) {
                String peerId = String.format("peer%d.%s.example.com", index++, org.getName());
                Map<String, Object> onePeerConfig = new HashMap<>();
                onePeerConfig.put("url", peer.getAddress());

                Map<String, Object> grpcOptions = new HashMap<>();
                grpcOptions.put("ssl-target-name-override", peerId);
                grpcOptions.put("fail-fast", false);
                grpcOptions.put("allow-insecure", false);
                onePeerConfig.put("grpcOptions", grpcOptions);

                Map<String, Object> tlsCACerts = new HashMap<>();

                File tlsCaFile =
                        new File(
                                outputPath
                                        + File.separator
                                        + "peers-pem"
                                        + File.separator
                                        + peerId
                                        + "-cert.pem");
                writeContent(tlsCaFile, peer.getTlsCa());
                tlsCACerts.put("path", tlsCaFile.getAbsolutePath());
                onePeerConfig.put("tlsCACerts", tlsCACerts);

                peers.put(peerId, onePeerConfig);
            }
        }
        return peers;
    }

    private static Map<String, Object> getOrganizations(StubConfig stubConfig, String outputPath)
            throws IOException {
        /*
        organizations:
          org1:
            mspid: Org1MSP
            users:
              Admin:
                cert:
                  path: /Users/ca.org1.example.com-cert.pem
            peers:
              - peer0.org1.example.com
        * */
        Map<String, Object> organizations = new HashMap<>();
        for (StubConfig.Org org : stubConfig.getOrgs()) {
            String orgId = org.getName();
            Map<String, Object> oneOrgConfig = new HashMap<>();
            oneOrgConfig.put("mspid", org.getMspid());

            Map<String, Object> users = new HashMap<>();
            for (StubConfig.User user : org.getUsers()) {
                Map<String, Object> oneUserConfig = new HashMap<>();
                Map<String, String> path = new HashMap<>();

                File crtFile =
                        new File(
                                outputPath
                                        + File.separator
                                        + "accounts"
                                        + File.separator
                                        + user.getName()
                                        + File.separator
                                        + "account.crt");
                writeContent(crtFile, user.getCrt());
                path.put("path", crtFile.getAbsolutePath());
                oneUserConfig.put("cert", path);

                File keyFile =
                        new File(
                                outputPath
                                        + File.separator
                                        + "accounts"
                                        + File.separator
                                        + user.getName()
                                        + File.separator
                                        + "account.key");
                writeContent(keyFile, user.getKey());
                path.put("path", keyFile.getAbsolutePath());
                oneUserConfig.put("key", path);

                users.put(user.getName(), oneUserConfig);
            }
            oneOrgConfig.put("users", users);

            List<String> peerIds = new ArrayList<>();
            int index = 0;
            for (StubConfig.Peer peer : org.getPeers()) {
                peerIds.add(String.format("peer%d.%s.example.com", index++, org.getName()));
            }
            oneOrgConfig.put("peers", peerIds);

            organizations.put(orgId, oneOrgConfig);
        }
        return organizations;
    }

    public static void generateConfig(String stubConfigJson, String outputPath) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        StubConfig stubConfig = jsonMapper.readValue(stubConfigJson, StubConfig.class);

        Map<String, Object> config = new HashMap<>();
        config.put("version", VERSION);

        // Client section
        Map<String, Object> client = new HashMap<>();
        client.put("organization", stubConfig.getFabricServices().getChannelName());
        Map<String, Object> logging = new HashMap<>();
        logging.put("level", "info");
        client.put("logging", logging);
        config.put("client", client);

        // Channels section
        Map<String, Object> channels = getChannels(stubConfig);
        config.put("channels", channels);

        // Organizations section
        Map<String, Object> organizations = getOrganizations(stubConfig, outputPath);
        config.put("organizations", organizations);

        // Orderers section
        Map<String, Object> orderers = getOrders(stubConfig, outputPath);
        config.put("orderers", orderers);

        // Peers section
        Map<String, Object> peersConfig = getPeers(stubConfig, outputPath);
        config.put("peers", peersConfig);

        // Write to YAML file

        ObjectMapper yamlMapper =
                new ObjectMapper(
                        new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        yamlMapper.writeValue(new File(outputPath + File.separator + "config.yaml"), config);
    }
}
