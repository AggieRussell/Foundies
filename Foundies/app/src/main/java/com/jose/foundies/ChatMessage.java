package com.jose.foundies;

/**
 * Created by josec on 4/24/2017.
 */

import java.util.Random;

public class ChatMessage {

    public String id, sender, receiver, body, timestamp;
    public int notificationType;


    public boolean isMine;// Did I send the message.

    public ChatMessage(String id, String sender, String receiver,
                       String body, int notificationType, String timestamp) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.notificationType = notificationType;
        this.timestamp = timestamp;
    }


    public ChatMessage(){}


    public void setId(String id) {this.id = id;}
    public String getId(){
        return this.id;
    }

    public void setSender(String sender){
        this.sender = sender;
    }
    public String getSender(){
        return this.sender;
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public String getReceiver(){
        return this.receiver;
    }

    public void setBody(String body){
        this.body = body;
    }
    public String getBody(){
        return this.body;
    }

    public void setNotificationType(int notificationType){this.notificationType = notificationType;}
    public int getNotificationType(){
        return this.notificationType;
    }

    public void setTimestamp(String pass){
        this.timestamp = pass;
    }
    public String getTimestamp(){
        return this.timestamp;
    }


}