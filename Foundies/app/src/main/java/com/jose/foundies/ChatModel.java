package com.jose.foundies;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.System.out;

/**
 * Created by josec on 4/28/2017.
 */

public class ChatModel {
    private ChatMessage message;
    private String strResponseBody = "";

    //Creates a json object to add message to the mongo database
    public String jsonMessagePost(ChatMessage m){
        String jsonPost  = "{ \"chatMessage\": { \"_id\": \"" + m.getId() + "\", \"sender\":\"" + m.getSender() + "\", \"receiver\":\"" + m.getReceiver() + "\", \"body\":\"" + m.getBody()
                + "\", \"notificationType\":\"" + m.getNotificationType() + "\", \"timestamp\":\"" + m.getTimestamp() + "\" } }";
        return jsonPost;
    }

    public ArrayList<ChatMessage> parseJSONmessage(String response_str) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(strResponseBody);
            jArray = jObject.getJSONArray("chatMessage");
            ArrayList<ChatMessage> messages = new ArrayList<>();
            for (int i = 0; i < jArray.length(); ++i) {
                ChatMessage msg = new ChatMessage();
                JSONObject curr = new JSONObject(jArray.getString(i));
                if (curr.getString("_id").isEmpty()) {
                    return null;
                } else {
                    msg.setId(curr.getString("_id"));
                    msg.setSender(curr.getString("sender"));
                    msg.setReceiver(curr.getString("receiver"));
                    msg.setBody(curr.getString("body"));
                    msg.setNotificationType(curr.getInt("notificationType"));
                    msg.setTimestamp(curr.getString("timestamp"));
                    messages.add(msg);
                }
            }
            return messages;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    //Make sure to implement
    //Looking message by sender
    public ArrayList<ChatMessage> getMessagesbyUsername(String receiver){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getMessagesbyUser(receiver);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                    System.out.println("This is the response body" + strResponseBody);
                    return parseJSONmessage(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    // use to get messages matching sender and reciever
    public ArrayList<ChatMessage> getMessages(ChatMessage message) throws ParseException {
        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getMessage();
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    strResponseBody = response.body().string();
                    out.println("    RESPONSE FOR getMessage:" + strResponseBody);
                    parseJSONmessage(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Added message to database
    public void postToMessages(ChatMessage message){

        final HerokuService service = Utility.connectAPI();
        String jsonPost = jsonMessagePost(message);

        //Used for connecting to the network so that Post can go through
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), jsonPost);
            Call<ResponseBody> call = service.createMessage(requestBody);

            try {
                Response<ResponseBody> response = call.execute();

                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                }
            } catch (IOException e) {
                // ...
            }
        }
    }

    //Delete message from database
    public void deleteToMessage(String id) {

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Call<ResponseBody> call = service.deleteMessage(id);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                }
            } catch (IOException e) {
                // ...
            }
        }
    }

}
