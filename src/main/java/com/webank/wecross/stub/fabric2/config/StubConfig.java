package com.webank.wecross.stub.fabric2.config;

import java.util.List;

public class StubConfig {
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
        public String adminName;
        public List<String> endorsers;
    }

    public FabricServices fabricServices;
    public List<Org> orgs;
}
