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

    public ChatMessage parseJSONmessage(String response_str) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(response_str);
            jArray = jObject.getJSONArray("chatMessage");
            ChatMessage message = new ChatMessage();
            if(jArray.isNull(0)){
                return null;
            }
            JSONObject curr = new JSONObject(jArray.getString(0));
            message.setId(curr.getString("_id"));
            message.setSender(curr.getString("sender"));
            message.setReceiver(curr.getString("receiver"));
            message.setBody(curr.getString("body"));
            message.setNotificationType(curr.getInt("notificationType"));
            message.setTimestamp(curr.getString("timestamp"));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    //Make sure to implement
    //Looking message by sender
    public ChatMessage getMessagesbyUsername(String id){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getMessagesbyUsername(id);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
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
