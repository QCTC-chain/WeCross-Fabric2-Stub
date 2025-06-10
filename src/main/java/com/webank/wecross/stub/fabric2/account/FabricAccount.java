package com.webank.wecross.stub.fabric2.account;

import com.webank.wecross.stub.Account;
import com.webank.wecross.stub.fabric2.common.FabricType;

public class FabricAccount implements Account {
    private int keyID;
    private boolean isDefault;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getType() {
        return FabricType.Account.FABRIC_ACCOUNT;
    }

    @Override
    public String getIdentity() {
        return "";
    }

    @Override
    public int getKeyID() {
        return this.keyID;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
