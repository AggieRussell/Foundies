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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserModel {
    
    public void UserModel(){}

    //Creates a json object to add user to the mongo database
    public static String jsonUserPost(Contact c){
        String jsonPost  = "{ \"user\": { \"_id\": \"" + Utility.uniqueID() + "\", \"username\":\"" + c.getEmail() + "\", \"first_name\":\"" + c.getFname() + "\", \"last_name\":\"" + c.getLname() + "\" } }";
        return jsonPost;
    }

    public static ArrayList<FoundItem> getFoundItems(){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getFoundItems();
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                    System.out.println(strResponseBody);
                    return parseJSON(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<FoundItem> parseJSON(String response_str) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(response_str);
            jArray = jObject.getJSONArray("items");
            ArrayList<FoundItem> foundItems = new ArrayList<FoundItem>();
            for (int i = 0; i < jArray.length(); ++i) {
                FoundItem found = new FoundItem();
                JSONObject curr = new JSONObject(jArray.getString(i));
                found.setId(curr.getString("_id"));
                found.setCategory1(curr.getString("category1"));
                found.setCategory2(curr.getString("category2"));
                found.setCategory3(curr.getString("category3"));
                found.setLat(curr.getString("latitude"));
                found.setLng(curr.getString("longitude"));
                found.setTimestamp(curr.getString("timestamp"));
                found.setUser(curr.getString("username"));
                foundItems.add(found);
            }
            return foundItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


        //Added user to database
    public static void postToAPI(String jsonPost){

        final HerokuService service = Utility.connectAPI();

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

    //Delete user from database
    public static void deleteToAPI(String username) {

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
