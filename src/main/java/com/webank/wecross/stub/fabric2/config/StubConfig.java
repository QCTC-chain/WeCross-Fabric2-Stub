package com.webank.wecross.stub.fabric2.config;

import java.util.List;

public class StubConfig {
    public static class User {
        public String name;
        public String mspid;
        public String crtFile;
        public String keyFile;
    }

    public static class FabricServices {
        public String channelName;
        public String orgUserName;
        public String ordererTlsCaFile;
        public List<String> ordererAddress;
    }

    public static class Org {
        public String id;
        public String name;
        public String tlsCaFile;
        public User admin;
        public List<String> endorsers;
    }

    public FabricServices fabricServices;
    public List<Org> orgs;
}
