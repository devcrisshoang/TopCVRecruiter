package com.example.topcvrecruiter.Model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    private int id;

    @SerializedName("sender_ID")
    private int sender_ID;

    @SerializedName("receiver_ID")
    private int receiver_ID;

    @SerializedName("content")
    private String content;

    @SerializedName("status")
    private Boolean status;

    @SerializedName("send_Time")
    private String send_Time;

    public Message(int id, int sender_ID, int receiver_ID, String content, Boolean status, String send_Time) {
        this.id = id;
        this.sender_ID = sender_ID;
        this.receiver_ID = receiver_ID;
        this.content = content;
        this.status = status;
        this.send_Time = send_Time;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSender_ID() { return sender_ID; }
    public void setSender_ID(int sender_ID) { this.sender_ID = sender_ID; }

    public int getReceiver_ID() { return receiver_ID; }
    public void setReceiver_ID(int receiver_ID) { this.receiver_ID = receiver_ID; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getSend_Time() { return send_Time; }
    public void setSend_Time(String send_Time) { this.send_Time = send_Time; }

}
