package com.webank.wecross.stub.fabric2.account;

import static com.webank.wecross.stub.fabric2.common.FabricType.GM_SM3_STUB_NAME;
import static com.webank.wecross.stub.fabric2.common.FabricType.GM_STUB_NAME;
import static com.webank.wecross.stub.fabric2.common.FabricType.STUB_NAME;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricAccountFactory {
    private static Logger logger = LoggerFactory.getLogger(FabricAccountFactory.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static FabricAccount build(Map<String, Object> properties) {
        try {
            Map<String, String> ext0Mapper =
                    objectMapper.readValue(
                            (String) properties.get("ext0"),
                            new TypeReference<Map<String, String>>() {});
            // String username = (String) properties.get("username");
            String username = ext0Mapper.get("userName");
            String mspID = ext0Mapper.get("mspID");
            String orgName = ext0Mapper.get("orgName");
            Integer keyID = (Integer) properties.get("keyID");
            String type = (String) properties.get("type");
            Boolean isDefault = (Boolean) properties.get("isDefault");
            String pubKey = (String) properties.get("pubKey");
            String secKey = (String) properties.get("secKey");

            if (!type.equals(STUB_NAME)
                    && !type.equals(GM_STUB_NAME)
                    && !type.equals(GM_SM3_STUB_NAME)) {
                logger.error("Invalid stub type: " + type);
                return null;
            }

            if (username == null || username.length() == 0) {
                logger.error("username has not given");
                return null;
            }

            if (mspID == null || mspID.length() == 0) {
                logger.error("mspID has not given in ext0");
                return null;
            }

            if (orgName == null || orgName.length() == 0) {
                logger.error("orgName has not given in ext0");
                return null;
            }

            if (keyID == null) {
                logger.error("keyID has not given");
                return null;
            }

            if (isDefault == null) {
                logger.error("isDefault has not given");
                return null;
            }

            if (pubKey == null || pubKey.length() == 0) {
                logger.error("pubKey has not given");
                return null;
            }

            if (secKey == null || secKey.length() == 0) {
                logger.error("secKey has not given");
                return null;
            }
            FabricAccount account = build(username, orgName, mspID, pubKey, secKey);
            account.setKeyID(keyID);
            account.setDefault(isDefault);
            account.setType(type);
            return account;
        } catch (Exception e) {
            logger.error("创建账户失败: {}", e);
            return null;
        }
    }

    public static FabricAccount build(
            String name, String orgName, String mspID, String pubKey, String secKey) {
        return new FabricAccount(name, orgName, mspID, pubKey, secKey);
    }
}
