package com.webank.wecross.stub.fabric2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecross.stub.Account;
import com.webank.wecross.stub.Connection;
import com.webank.wecross.stub.Driver;
import com.webank.wecross.stub.Stub;
import com.webank.wecross.stub.StubFactory;
import com.webank.wecross.stub.WeCrossContext;
import com.webank.wecross.stub.fabric2.account.FabricAccount;
import com.webank.wecross.stub.fabric2.account.FabricAccountFactory;
import com.webank.wecross.stub.fabric2.utils.ConfigUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.StringJoiner;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Stub("Fabric2.0")
public class FabricStubFactory implements StubFactory {
    private static Logger logger = LoggerFactory.getLogger(FabricStubFactory.class);

    private String stubPath;

    @Override
    public void init(WeCrossContext context) {}

    @Override
    public Driver newDriver() {
        return new FabricDriver();
    }

    @Override
    public Connection newConnection(String path) {
        try {
            if (path.contains("classpath:")) {
                this.stubPath = ConfigUtils.classpath2Absolute(path);
            } else {
                this.stubPath = path;
            }

            FabricConnection fabricConnection = FabricConnectionFactory.build(path);
            fabricConnection.start();

            // Check proxy chaincode
            if (!fabricConnection.hasProxyDeployed2AllPeers()) {
                throw new Exception("WeCrossProxy has not been deployed to all org");
            }

            return fabricConnection;
        } catch (Exception e) {
            logger.error("newConnection exception: " + e);
            return null;
        }
    }

    @Override
    public Account newAccount(Map<String, Object> properties) {
        FabricAccount account = FabricAccountFactory.build(properties);
        try {
            File certFile =
                    new File(
                            this.stubPath
                                    + File.separator
                                    + "accounts"
                                    + File.separator
                                    + account.getIdentity()
                                    + File.separator
                                    + "account.crt");
            writeContent(certFile, account.getPubKey());

            File keyFile =
                    new File(
                            this.stubPath
                                    + File.separator
                                    + "accounts"
                                    + File.separator
                                    + account.getIdentity()
                                    + File.separator
                                    + "account.key");
            writeContent(keyFile, account.getSecKey());
        } catch (IOException e) {
        }

        return account;
    }

    @Override
    public void generateAccount(String path, String[] args) {
        try {
            String chainType = "Fabric2.0";
            String mspId = "Org1MSP";
            if (args.length == 2) {
                chainType = args[0];
                mspId = args[1];
            }
            // Generate config file only, copy user cert from crypto-config/

            // Write config file
            String accountTemplate =
                    "[account]\n"
                            + "    type = '"
                            + chainType
                            + "'\n"
                            + "    mspid = '"
                            + mspId
                            + "'\n"
                            + "    keystore = 'account.key'\n"
                            + "    signcert = 'account.crt'";

            String confFilePath = path + "/account.toml";
            File confFile = new File(confFilePath);
            if (!confFile.createNewFile()) {
                logger.error("Conf file exists! {}", confFile);
                return;
            }

            FileWriter fileWriter = new FileWriter(confFile);
            try {
                fileWriter.write(accountTemplate);
            } finally {
                fileWriter.close();
            }

            String name = new File(path).getName();
            System.out.println(
                    "\nSUCCESS: Account \""
                            + name
                            + "\" config framework has been generated to \""
                            + path
                            + "\"\nPlease copy cert file and edit account.toml");

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    private void writeContent(File file, String content) throws IOException {
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

    private String generateTomlStr(
            String chainType,
            String chainName,
            Map<String, Object> stubConfig,
            Map<String, Object> mqConfig) {

        StringJoiner toml = new StringJoiner("\n");

        String stubCommon =
                "[common]\n"
                        + "    name = '"
                        + chainName
                        + "'\n"
                        + "    type = '"
                        + chainType
                        + "'\n";
        toml.add(stubCommon);

        Map<String, String> fabricServiceObject =
                (Map<String, String>) stubConfig.get("fabricServices");

        String fabricService =
                "[fabricServices]\n"
                        + "    channelName = '"
                        + fabricServiceObject.get("channelName")
                        + "'\n"
                        + "    orgUserName = '"
                        + fabricServiceObject.get("orgUserName")
                        + "'\n";
        toml.add(fabricService);

        String mq =
                "[mq]\n"
                        + "    # kafka/rabbitmq/rocketmq\n"
                        + "    type = '"
                        + mqConfig.get("type")
                        + "'\n"
                        + "    host = '"
                        + mqConfig.get("host")
                        + "'\n"
                        + "    port = "
                        + mqConfig.get("port")
                        + "\n"
                        + "    userName = '"
                        + mqConfig.get("userName")
                        + "'\n"
                        + "    password = '"
                        + mqConfig.get("password")
                        + "'\n"
                        + "    topic = '"
                        + mqConfig.get("topic")
                        + "'\n"
                        + "    group = '"
                        + mqConfig.get("group")
                        + "'";
        toml.add(mq);

        return toml.toString();
    }

    @Override
    public void generateConnection(String path, String[] args) {
        try {
            String chainType = args[0];
            String chainName = args[1];
            String stubConfigStr = args[2];
            String mqConfigStr = args[3];
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> stubConfig =
                    objectMapper.readValue(
                            stubConfigStr, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> mqConfig =
                    objectMapper.readValue(
                            mqConfigStr, new TypeReference<Map<String, Object>>() {});
            String stubTomlContent =
                    generateTomlStr(
                            chainType,
                            chainName,
                            stubConfig,
                            (Map<String, Object>) mqConfig.get("mq"));
            File confFile = new File(path + File.separator + "stub.toml");
            writeContent(confFile, stubTomlContent);

            StubConfigGenerator.generateConfig(stubConfigStr, path);

            // Generate proxy and hub chaincodes
            generateProxyChaincodes(path);
            generateHubChaincodes(path);

            System.out.println(
                    "SUCCESS: Chain \""
                            + chainName
                            + "\" config framework has been generated to \""
                            + path
                            + "\"\nPlease copy cert file and edit stub.toml");
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void generateProxyChaincodes(String path) {
        String srcPath = "chaincode-fabric2.0" + File.separator + "WeCrossProxy";
        String destPath =
                path
                        + File.separator
                        + srcPath
                        + File.separator
                        + "src"
                        + File.separator
                        + "github.com"
                        + File.separator
                        + "WeCrossProxy";
        copyJarDir(srcPath, destPath);
    }

    public void generateHubChaincodes(String path) {

        String srcPath = "chaincode-fabric2.0" + File.separator + "WeCrossHub";
        String destPath =
                path
                        + File.separator
                        + srcPath
                        + File.separator
                        + "src"
                        + File.separator
                        + "github.com"
                        + File.separator
                        + "WeCrossHub";
        copyJarDir(srcPath, destPath);
    }

    private static void copyJarDir(String srcPath, String destPath) {
        try {
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(srcPath + File.separator + "**");
            for (Resource resource : resources) {
                URL url = resource.getURL();

                String destFileName;
                String[] split = url.getFile().split(srcPath);
                if (split.length == 2) {
                    if (split[1].endsWith("" + File.separator)) {
                        continue;
                    }
                    destFileName = split[1];
                } else if (split.length < 2) {
                    continue;
                } else {
                    destFileName = url.getFile().replace(split[0], "");
                }

                String destFile = destPath + File.separator + destFileName;

                // System.out.println("Copy " + url.getFile() + " to " + destFile);
                System.out.print(".");

                File dest = new File(destFile);
                try {
                    FileUtils.copyURLToFile(url, dest);
                } catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(
                "This is Fabric2.0 Stub Plugin. Please copy this file to router/plugin/");
        System.out.println("To deploy WeCrossProxy:");
        System.out.println(
                "    java -cp conf/:lib/*:plugin/* com.webank.wecross.stub.fabric2.proxy.ProxyChaincodeDeployment ");
        System.out.println("To performance test, please run the command for more info:");
        // System.out.println(
        //        "    Pure:    java -cp conf/:lib/*:plugin/* " + PerformanceTest.class.getName());
        // System.out.println(
        //        "    Proxy:   java -cp conf/:lib/*:plugin/* " + ProxyTest.class.getName());
    }
}
