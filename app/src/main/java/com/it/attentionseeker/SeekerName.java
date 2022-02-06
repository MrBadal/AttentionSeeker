package com.it.attentionseeker;

public class SeekerName {
    String seekerName;
    String seekerId;
    String seekerNotification;

    public String getSeekerNotification() {
        return seekerNotification;
    }

    public void setSeekerNotification(String seekerNotification) {
        this.seekerNotification = seekerNotification;
    }

    public SeekerName(String seekerName) {
        this.seekerName = seekerName;
        this.seekerId = seekerId;
    }

    public String getSeekerName() {
        return seekerName;
    }

    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }

}
