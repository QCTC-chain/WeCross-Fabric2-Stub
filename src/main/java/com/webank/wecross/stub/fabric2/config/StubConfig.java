package com.webank.wecross.stub.fabric2.config;

import java.util.List;

public class StubConfig {
    public static class FabricServices {
        private String channelName;
        private String userOrgName;
        // Getters and setters
        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getUserOrgName() {
            return userOrgName;
        }

        public void setUserOrgName(String channelName) {
            this.userOrgName = userOrgName;
        }
    }

    public static class Order {
        private String domain;
        private String tlsCa;
        private String address;

        public String getTlsCa() {
            return tlsCa;
        }

        public void setTlsCa(String tlsCa) {
            this.tlsCa = tlsCa;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    public static class Org {
        private String name;
        private String mspid;
        private List<String> peers;
        private List<User> users;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMspid() {
            return mspid;
        }

        public void setMspid(String mspid) {
            this.mspid = mspid;
        }

        public List<String> getPeers() {
            return peers;
        }

        public void setPeers(List<String> peers) {
            this.peers = peers;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public static class Peer {
        private String orgName;
        private String tlsCa;
        private String address;
        private String domain;

        // Getters and setters
        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getTlsCa() {
            return tlsCa;
        }

        public void setTlsCa(String tlsCa) {
            this.tlsCa = tlsCa;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    public static class User {
        // private boolean isAdmin;
        private String name;
        private String crt;
        private String key;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCrt() {
            return crt;
        }

        public void setCrt(String crt) {
            this.crt = crt;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private FabricServices fabricServices;
    private List<Org> orgs;
    private List<Order> orders;
    private List<Peer> peers;

    // Getters and setters
    public FabricServices getFabricServices() {
        return fabricServices;
    }

    public void setFabricServices(FabricServices fabricServices) {
        this.fabricServices = fabricServices;
    }

    public List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Org> orgs) {
        this.orgs = orgs;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public void setPeers(List<Peer> peers) {
        this.peers = peers;
    }
}
