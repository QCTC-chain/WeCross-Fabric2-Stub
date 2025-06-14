package com.webank.wecross.stub.fabric2.config;

public class StubConfig {
    public static class FabricServices {
        private String channelName;
        private String orgUserName;
        private String ordererTlsCa;
        private String ordererAddress;

        // Getters and setters
        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getOrgUserName() {
            return orgUserName;
        }

        public void setOrgUserName(String orgUserName) {
            this.orgUserName = orgUserName;
        }

        public String getOrdererTlsCa() {
            return ordererTlsCa;
        }

        public void setOrdererTlsCa(String ordererTlsCa) {
            this.ordererTlsCa = ordererTlsCa;
        }

        public String getOrdererAddress() {
            return ordererAddress;
        }

        public void setOrdererAddress(String ordererAddress) {
            this.ordererAddress = ordererAddress;
        }
    }

    public static class Org {
        private String name;
        private String mspid;
        private java.util.List<Peer> peers;
        private java.util.List<User> users;

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

        public java.util.List<Peer> getPeers() {
            return peers;
        }

        public void setPeers(java.util.List<Peer> peers) {
            this.peers = peers;
        }

        public java.util.List<User> getUsers() {
            return users;
        }

        public void setUsers(java.util.List<User> users) {
            this.users = users;
        }
    }

    public static class Peer {
        private String tlsCa;
        private String address;

        // Getters and setters
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
    private java.util.List<Org> orgs;

    // Getters and setters
    public FabricServices getFabricServices() {
        return fabricServices;
    }

    public void setFabricServices(FabricServices fabricServices) {
        this.fabricServices = fabricServices;
    }

    public java.util.List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(java.util.List<Org> orgs) {
        this.orgs = orgs;
    }
}
