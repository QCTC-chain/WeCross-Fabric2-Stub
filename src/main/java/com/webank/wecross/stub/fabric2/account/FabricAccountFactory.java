package com.webank.wecross.stub.fabric2.account;

import static com.webank.wecross.stub.fabric2.common.FabricType.STUB_NAME;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricAccountFactory {
    private static Logger logger = LoggerFactory.getLogger(FabricAccountFactory.class);

    public static FabricAccount build(Map<String, Object> properties) {
        String username = (String) properties.get("username");
        String mspID = (String) properties.get("ext0");
        Integer keyID = (Integer) properties.get("keyID");
        String type = (String) properties.get("type");
        Boolean isDefault = (Boolean) properties.get("isDefault");
        String pubKey = (String) properties.get("pubKey");
        String secKey = (String) properties.get("secKey");

        if (!type.equals(STUB_NAME)) {
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
        FabricAccount account = build(username, mspID, pubKey, secKey);
        account.setKeyID(keyID);
        account.setDefault(isDefault);
        return account;
    }

    public static FabricAccount build(String name, String mspID, String pubKey, String secKey) {
        return new FabricAccount(name, mspID, pubKey, secKey);
    }
}
