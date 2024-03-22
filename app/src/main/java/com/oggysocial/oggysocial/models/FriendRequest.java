package com.oggysocial.oggysocial.models;

import java.util.Date;

public class FriendRequest extends Notify {
    String senderId;
    String receiverId;
    Date date;

    public FriendRequest() {
    }

    public FriendRequest(String senderId, String receiverId, Date date) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public Date getDate() {
        return date;
    }
}
