package com.jose.foundies;

import android.os.StrictMode;

import java.io.IOException;

/**
 * Created by kylepreston on 3/27/17.
 */

import android.content.Intent;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserModel {

    private Contact user;

    public Contact getUser() {
        return user;
    }

    public void setUser(Contact user) {
        this.user = user;
    }

    private int numOfSearches;
    //Used for saving email to the database

    public void UserModel(){
        numOfSearches = 0;
    }

    public String getUserEmail(){ return user.getEmail();}

    private String uniqueId(){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }



    //Creates a json object to add user to the mongo database
    public String jsonUserPost(Contact c){
        String jsonPost  = "{ \"user\": { \"_id\": \"" + uniqueId() + "\", \"username\":\"" + c.getEmail() + "\", \"first_name\":\"" + c.getFname() + "\", \"last_name\":\"" + c.getLname()
               + "\", \"password\":\"" + c.getPass() + "\" } }";
        return jsonPost;
    }

    //Looking up a user by their username
    public Contact getUserByUsername(String id){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getUsersByUsername(id);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                    return parseJSONuser(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Contact parseJSONuser(String response_str) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(response_str);
            jArray = jObject.getJSONArray("user");
            Contact user = new Contact();
            if(jArray.isNull(0)){
                return null;
            }
            JSONObject curr = new JSONObject(jArray.getString(0));
            user.setId(curr.getString("_id"));
            user.setEmail(curr.getString("username"));
            user.setFname(curr.getString("first_name"));
            user.setLname(curr.getString("last_name"));
            user.setPassword(curr.getString("password"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    //Added user to database
    public void postToAPI(Contact contact){

        final HerokuService service = Utility.connectAPI();
        String jsonPost = jsonUserPost(contact);

        //Used for connecting to the network so that Post can go through
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), jsonPost);
            Call<ResponseBody> call = service.createUser(requestBody);

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

    //Added user to database
    public void updateUserInfo(String username, Map<String, String> updatedParams){

        final HerokuService service = Utility.connectAPI();

        //Used for connecting to the network so that Post can go through
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.updateUser(username, updatedParams);
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

    //Delete user from database
    public void deleteToAPI(String username) {

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Call<ResponseBody> call = service.deleteUser(username);
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
