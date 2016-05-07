package org.ivandgetic.fivechess.models;

/**
 * Created by ivandgetic on 14/7/10.
 */
public class User {
    String name;
    String state;
    String remoteIP;

    public User(String name, String state, String remoteIP) {
        this.name = name;
        this.state = state;
        this.remoteIP = remoteIP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }
}
