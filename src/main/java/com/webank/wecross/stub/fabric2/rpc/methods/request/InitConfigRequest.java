package com.webank.wecross.stub.fabric2.rpc.methods.request;

import com.webank.wecross.stub.fabric2.rpc.service.FabricService;
import java.util.List;

public class InitConfigRequest {
    public static class FabricService {
        public String channelName;
        public String orgUserName;
        public String ordererTlsCa;
        public String ordererAddress;
    }

    public static class User {
        public String name;
        public String crt;
        public String key;
    }

    public static class Peer {
        public String tlsCa;
        public String address;
    }

    public static class Org {
        public String name;
        public String mspid;
        public List<Peer> peers;
        public List<User> users;
    }

    public static class MQ {
        public String type;
        public String host;
        public Long port;
        public String topic;
        public String group;
    }

    public String chainName;
    public FabricService fabricServices;
    public List<Org> orgs;
    public MQ mq;
}
