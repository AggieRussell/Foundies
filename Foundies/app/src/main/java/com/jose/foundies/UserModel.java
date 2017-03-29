package com.jose.foundies;

import android.os.StrictMode;

import java.io.IOException;

/**
 * Created by kylepreston on 3/27/17.
 */

import android.content.Intent;
import android.os.StrictMode;

import org.json.JSONArray;
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

    public ArrayList<FoundItem> getFoundItems(){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), jsonPost);
            Call<ResponseBody> call = service.getUsers();
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                    return parseJSON(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<FoundItem> parseJSON(response_str) {
        ArrayList<FoundItem> foundItems = new ArrayList<FoundItem>();
        JSONObject jObject;
        JSONArray jArray;
        try {
        jObject = new JSONObject(response_str);
        jArray = jObject.getJSONArray("users");
        for(int i=0; i<jArray.length(); ++i) {
            ArrayList<String> newUser = new ArrayList<String>();
            JSONObject curr = new JSONObject(jArray.getString(i));
            newUser.add(curr.getString("_id"));
            newUser.add(curr.getString("first_name"));
            newUser.add(curr.getString("last_name"));
            newUser.add(curr.getString("username"));
            dbResults.add(newUser);
            //updateDisplay();
        }
        createDisplay();
        }
        catch (JSONException e) {
            e.printStackTrace();
            greeting.setText(e.getMessage());
        }
        out.println("Ending parse JSON...");
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
