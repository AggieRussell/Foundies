package com.jose.foundies;

/**
 * Created by josec on 4/24/2017.
 */

import java.util.Random;

public class ChatMessage {

    public String body, sender, receiver, senderName;
    public String Date, Time;
    public String msgid;

    public boolean isMine;// Did I send the message.

    public ChatMessage(String sender, String receiver, String body,
                       String msgid, boolean isMINE) {
        this.body = body;
        this.isMine = isMINE;
        this.sender = sender;
        this.msgid = msgid;
        this.receiver = receiver;
        this.senderName = sender;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));

    }
}