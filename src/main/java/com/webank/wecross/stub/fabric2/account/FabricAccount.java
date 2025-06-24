package com.webank.wecross.stub.fabric2.account;

import com.webank.wecross.stub.Account;
import java.nio.charset.StandardCharsets;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;

public class FabricAccount implements Account {
    private int keyID;
    private boolean isDefault;

    private String orgName;
    private String userName;
    private String mspID;
    private String pubKey;
    private String secKey;

    private String type;

    public FabricAccount(
            String userName, String orgName, String mspID, String pubKey, String secKey) {
        this.userName = userName;
        this.mspID = mspID;
        this.orgName = orgName;
        this.pubKey = pubKey;
        this.secKey = secKey;
    }

    @Override
    public String getName() {
        return this.userName;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getIdentity() {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(this.pubKey.getBytes(StandardCharsets.UTF_8), 0, this.pubKey.length());
        byte[] address = kecc.digest();
        return Hex.toHexString(address);
    }

    @Override
    public int getKeyID() {
        return this.keyID;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getPubKey() {
        return this.pubKey;
    }

    public String getSecKey() {
        return this.secKey;
    }

    public String getMspID() {
        return this.mspID;
    }

    public String getOrgName() {
        return this.orgName;
    }
}
