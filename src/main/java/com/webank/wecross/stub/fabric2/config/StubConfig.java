package com.webank.wecross.stub.fabric2.config;

import java.util.List;

public class StubConfig {
    public static class User {
        public boolean isAdmin;
        public String name;
        public String crt;
        public String key;
    }

    public static class Peer {
        public String tlsCa;
        public String address;
    }

    public static class FabricServices {
        public String channelName;
        public String orgUserName;
        public String ordererTlsCa;
        public String ordererAddress;
    }

    public static class Org {
        public String name;
        public String mspid;
        public List<Peer> peers;
        public List<User> users;
    }

    public FabricServices fabricServices;
    public List<Org> orgs;
}
