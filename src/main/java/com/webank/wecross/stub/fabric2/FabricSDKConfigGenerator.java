package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.moandjiezana.toml.Toml;
import com.webank.wecross.stub.fabric2.account.FabricAccount;
import com.webank.wecross.stub.fabric2.config.StubConfig;
import com.webank.wecross.stub.fabric2.utils.ConfigUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabricSDKConfigGenerator {
    private static final String VERSION = "1.0.0";

    private static Map<String, String> cacheSDKConfig = new HashMap<>();

    private static ObjectMapper yamlObject =
            new ObjectMapper(
                    new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

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

    private static Map<String, Object> getEntityMatchers(StubConfig stubConfig, String chainType) {
        /*
        entitymatchers:
          orderer:
            - mappedHost: orderer.example.com
              pattern: orderer.example.com.(\w+)
              sslTargetOverrideUrlSubstitutionExp: orderer.example.com
              urlSubstitutionExp: grpcs://192.168.11.38:7050
          peer:
            - mappedHost: peer0.org1.example.com
              pattern: peer0.org1.example.com.(\w+)
              sslTargetOverrideUrlSubstitutionExp: peer0.org1.example.com
              urlSubstitutionExp: grpcs://192.168.11.38:7051
        * */
        Map<String, Object> entityMatchers = new HashMap<>();

        List<Object> orders = new ArrayList<>();
        for (StubConfig.Order order : stubConfig.getOrders()) {
            Map<String, Object> entity = new HashMap<>();
            entity.put("mappedHost", order.getDomain());
            if ("GM_Fabric2.0".equals(chainType)) {
                // 联通的 fabirc2 链配置
                entity.put("pattern", order.getDomain() + ".(\\w+)");
            } else {
                entity.put("pattern", order.getDomain() + ":(\\d+)");
            }
            // entity.put("sslTargetOverrideUrlSubstitutionExp", order.getDomain());
            // entity.put("urlSubstitutionExp", order.getAddress());
            orders.add(entity);
        }
        entityMatchers.put("orderer", orders);

        List<Object> peers = new ArrayList<>();
        for (StubConfig.Peer peer : stubConfig.getPeers()) {
            Map<String, Object> entity = new HashMap<>();
            entity.put("mappedHost", peer.getDomain());
            if ("GM_Fabric2.0".equals(chainType)) {
                entity.put("pattern", peer.getDomain() + ".(\\w+)");
            } else {
                entity.put("pattern", peer.getDomain() + ":(\\d+)");
            }
            // entity.put("sslTargetOverrideUrlSubstitutionExp", peer.getDomain());
            // entity.put("urlSubstitutionExp", peer.getAddress());
            peers.add(entity);
        }
        entityMatchers.put("peer", peers);

        return entityMatchers;
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

        // TODO
        // 测试
        String userOrg = stubConfig.getFabricServices().getUserOrgName();

        Map<String, Object> channelConfig = new HashMap<>();

        Map<String, Object> onePeerFixedConfig = new HashMap<>();
        onePeerFixedConfig.put("endorsingPeer", true);
        onePeerFixedConfig.put("chaincodeQuery", true);
        onePeerFixedConfig.put("ledgerQuery", true);
        onePeerFixedConfig.put("eventSource", true);
        Map<String, Object> peers = new HashMap<>();
        for (StubConfig.Peer peer : stubConfig.getPeers()) {
            if (userOrg.equals(peer.getOrgName())) {
                peers.put(peer.getDomain(), onePeerFixedConfig);
            }
        }
        channelConfig.put("peers", peers);
        channels.put(channelId, channelConfig);
        return channels;
    }

    private static Map<String, Object> getOrders(StubConfig stubConfig) throws IOException {
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
        for (StubConfig.Order order : stubConfig.getOrders()) {
            String orderId = order.getDomain();

            Map<String, Object> orderConfig = new HashMap<>();
            orderConfig.put("url", order.getAddress());

            Map<String, Object> grpcOptions = new HashMap<>();
            grpcOptions.put("ssl-target-name-override", orderId);
            grpcOptions.put("fail-fast", false);
            grpcOptions.put("allow-insecure", false);
            orderConfig.put("grpcOptions", grpcOptions);

            Map<String, Object> tlsCACerts = new HashMap<>();
            tlsCACerts.put("path", order.getTlsCa());
            orderConfig.put("tlsCACerts", tlsCACerts);
            orders.put(orderId, orderConfig);
        }

        return orders;
    }

    private static Map<String, Object> getPeers(StubConfig stubConfig) throws IOException {
        /*
        * peers:
              peer0.org1.example.com:
                url: 192.168.11.38:7051
                grpcOptions:
                  ssl-target-name-override: peer0.org1.example.com
                  fail-fast: false
                  allow-insecure: false
                tlsCACerts:
                  path: /Users/org1CA/ca.org1.example.com-cert.crt
        */
        Map<String, Object> peers = new HashMap<>();
        for (StubConfig.Peer peer : stubConfig.getPeers()) {
            String domain = peer.getDomain();
            Map<String, Object> onePeerConfig = new HashMap<>();
            onePeerConfig.put("url", peer.getAddress());

            Map<String, Object> grpcOptions = new HashMap<>();
            grpcOptions.put("ssl-target-name-override", domain);
            grpcOptions.put("fail-fast", false);
            grpcOptions.put("allow-insecure", false);
            onePeerConfig.put("grpcOptions", grpcOptions);

            Map<String, Object> tlsCACerts = new HashMap<>();

            tlsCACerts.put("path", peer.getTlsCa());
            onePeerConfig.put("tlsCACerts", tlsCACerts);

            peers.put(domain, onePeerConfig);
        }
        return peers;
    }

    private static Map<String, Object> getOrganizations(StubConfig stubConfig) throws IOException {
        /*
        organizations:
          org1:
            mspid: Org1MSP
            users:
              Admin:
                cert:
                  path: /Users/ca.org1.example.com-cert.crt
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

                Map<String, String> certPath = new HashMap<>();
                certPath.put("path", user.getCrt());
                oneUserConfig.put("cert", certPath);

                Map<String, String> keyPath = new HashMap<>();
                keyPath.put("path", user.getKey());
                oneUserConfig.put("key", keyPath);

                users.put(user.getName(), oneUserConfig);
            }
            oneOrgConfig.put("users", users);

            List<String> peerIds = new ArrayList<>();
            for (String domain : org.getPeers()) {
                peerIds.add(domain);
            }
            oneOrgConfig.put("peers", peerIds);

            organizations.put(orgId, oneOrgConfig);
        }
        return organizations;
    }

    private static StubConfig makeStubConfigFrom(String stubConfigJson) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        Map<String, Object> stubConfigMap =
                jsonMapper.readValue(stubConfigJson, new TypeReference<Map<String, Object>>() {});

        StubConfig stubConfig = new StubConfig();

        Map<String, String> chainObject = (Map<String, String>) stubConfigMap.get("chain");

        List<StubConfig.Order> orders =
                jsonMapper.readValue(
                        jsonMapper.writeValueAsBytes(stubConfigMap.get("orders")),
                        new TypeReference<List<StubConfig.Order>>() {});
        stubConfig.setOrders(orders);

        List<StubConfig.Peer> peers =
                jsonMapper.readValue(
                        jsonMapper.writeValueAsBytes(stubConfigMap.get("peers")),
                        new TypeReference<List<StubConfig.Peer>>() {});
        stubConfig.setPeers(peers);

        Map<String, String> userObject = (Map<String, String>) stubConfigMap.get("user");
        StubConfig.Org org = new StubConfig.Org();
        org.setName(userObject.get("orgName"));
        org.setMspid(userObject.get("mspId"));

        StubConfig.FabricServices fabricServices = new StubConfig.FabricServices();
        fabricServices.setChannelName((String) chainObject.get("channelName"));
        fabricServices.setUserOrgName(userObject.get("orgName"));
        stubConfig.setFabricServices(fabricServices);

        StubConfig.User user = new StubConfig.User();
        user.setCrt(userObject.get("crt"));
        user.setKey(userObject.get("key"));
        user.setName(userObject.get("name"));
        List<StubConfig.User> users = new ArrayList<>();
        users.add(user);
        org.setUsers(users);
        List<String> peersInOrg = new ArrayList<>();
        for (StubConfig.Peer peer : stubConfig.getPeers()) {
            if (org.getName().equals(peer.getOrgName())) {
                peersInOrg.add(peer.getDomain());
            }
        }
        org.setPeers(peersInOrg);

        List<StubConfig.Org> orgs = new ArrayList<>();
        orgs.add(org);
        stubConfig.setOrgs(orgs);

        return stubConfig;
    }

    public static String getDefaultSDKConfig(String stubPath) throws Exception {
        String tomlPath = stubPath + File.separator + "stub.toml";
        return generateSDKConfigFrom(tomlPath);
    }

    public static String generateSDKConfigFrom(String stubTomlPath) throws Exception {
        Toml toml = ConfigUtils.getToml(stubTomlPath);
        Map<String, Object> map = toml.toMap();
        String chainType = toml.getString("common.type");
        map.remove("common");
        ObjectMapper objectMapper = new ObjectMapper();
        return generateSDKConfig(objectMapper.writeValueAsString(map), chainType);
    }

    public static String generateSDKConfig(String stubConfigJson, String chainType)
            throws IOException {
        StubConfig stubConfig = makeStubConfigFrom(stubConfigJson);

        Map<String, Object> config = new HashMap<>();
        config.put("version", VERSION);

        // Client section
        Map<String, Object> client = new HashMap<>();
        client.put("organization", stubConfig.getFabricServices().getUserOrgName());
        Map<String, Object> logging = new HashMap<>();
        logging.put("level", "info");
        client.put("logging", logging);
        config.put("client", client);

        // entitymatchers
        Map<String, Object> entitymatchers = getEntityMatchers(stubConfig, chainType);
        config.put("entitymatchers", entitymatchers);

        // Channels section
        Map<String, Object> channels = getChannels(stubConfig);
        config.put("channels", channels);

        // Organizations section
        Map<String, Object> organizations = getOrganizations(stubConfig);
        config.put("organizations", organizations);

        // Orderers section
        Map<String, Object> orderers = getOrders(stubConfig);
        config.put("orderers", orderers);

        // Peers section
        Map<String, Object> peersConfig = getPeers(stubConfig);
        config.put("peers", peersConfig);

        String yamlContent = yamlObject.writeValueAsString(config);

        return yamlContent;
    }

    /** 使用 stub.toml 和 account 获取更新 SDK config */
    public static String getOrUpdateSDKConfig(String stubPath, FabricAccount account)
            throws Exception {
        String cacheKey =
                String.format(
                        "%s-%s-%s", account.getOrgName(), account.getMspID(), account.getName());
        String sdkConfig = cacheSDKConfig.get(cacheKey);
        if (sdkConfig != null) {
            return sdkConfig;
        }

        String stubTomePath = stubPath + File.separator + "stub.toml";
        Toml stubToml = ConfigUtils.getToml(stubTomePath);
        List<Map<String, String>> peersMapper = stubToml.getList("peers");
        String stubConfigStr = generateSDKConfigFrom(stubTomePath);

        Map<String, Object> sdkConfigMapper =
                yamlObject.readValue(stubConfigStr, new TypeReference<Map<String, Object>>() {});

        // 更新 client 的组织名称
        Map<String, Object> client = (Map<String, Object>) sdkConfigMapper.get("client");
        client.put("organization", account.getOrgName());

        // 更新 organizations 中的组织和用户
        Map<String, Object> organizations =
                (Map<String, Object>) sdkConfigMapper.get("organizations");
        organizations.clear();

        if (stubPath.contains("classpath:")) {
            stubPath = ConfigUtils.classpath2Absolute(stubPath);
        }
        // 保存用户的 cert
        File crtFile =
                new File(
                        stubPath
                                + File.separator
                                + "accounts"
                                + File.separator
                                + account.getOrgName()
                                + File.separator
                                + account.getName()
                                + File.separator
                                + "account.crt");
        writeContent(crtFile, account.getPubKey());
        Map<String, Object> userCrtAndKey = new HashMap<>();
        Map<String, String> userCrtPath = new HashMap<>();
        userCrtPath.put("path", crtFile.getAbsolutePath());
        userCrtAndKey.put("cert", userCrtPath);

        // 保存用户的 key
        File keyFile =
                new File(
                        stubPath
                                + File.separator
                                + "accounts"
                                + File.separator
                                + account.getOrgName()
                                + File.separator
                                + account.getName()
                                + File.separator
                                + "account.key");
        writeContent(keyFile, account.getSecKey());
        Map<String, String> userKeyPath = new HashMap<>();
        userKeyPath.put("path", keyFile.getAbsolutePath());
        userCrtAndKey.put("key", userKeyPath);

        // 更新 users
        Map<String, Object> users = new HashMap<>();
        users.put(account.getName(), userCrtAndKey);

        // 跟新 peers
        List<String> peers = new ArrayList<>();
        for (Map<String, String> peer : peersMapper) {
            if (account.getOrgName().equals(peer.get("orgName"))) {
                peers.add(peer.get("domain"));
            }
        }

        // 更新 organizations
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("mspid", account.getMspID());
        objectMap.put("peers", peers);
        objectMap.put("users", users);
        organizations.put(account.getOrgName(), objectMap);

        sdkConfig = yamlObject.writeValueAsString(sdkConfigMapper);
        cacheSDKConfig.put(cacheKey, sdkConfig);
        return sdkConfig;
    }
}
