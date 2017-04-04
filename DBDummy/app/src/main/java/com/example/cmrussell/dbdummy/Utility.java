package com.example.cmrussell.dbdummy;

import android.content.Intent;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by kylepreston on 3/8/17.
 */

public final class Utility {
    private Utility() {
    }

    public static String uniqueId(){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    public static String jsonUserPost(String username, String firstName, String lastName){
        String jsonPost  = "{ \"user\": { \"_id\": \"" + uniqueId() + "\", \"username\":\"" + username + "\", \"first_name\":\"" + firstName + "\", \"last_name\":\"" + lastName + "\" } }";
        System.out.println("\n \n \n " + uniqueId() + "\n \n ");
        return jsonPost;
    }

    public static String jsonFoundPost(FoundItem f){
        String jsonPost  = "{ \"found\": { \"_id\": \"" + uniqueId() + "\", \"category1\":\"" + f.getCategory1()
                + "\", \"category2\":\"" + f.getCategory2() + "\", \"category3\":\""
                + f.getCategory3() + "\", \"username\":\"" + f.getUser() + "\", \"timestamp\":\""
                + f.getTimestamp() + "\", \"latitude\":\""
                + f.getLat() + "\", \"longitude\":\"" + f.getLng() + "\" } }";
        System.out.println("\n \n \n " + f.getCategory2() + "\n \n ");
        return jsonPost;
    }

    public static void postToAPI(String jsonPost){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

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

    public static void postToFound(String jsonPost){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

        //Used for connecting to the network so that Post can go through
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), jsonPost);
            Call<ResponseBody> call = service.createFoundItem(requestBody);
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

    public static ArrayList<FoundItem> getFoundItems(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

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

    public static User getUserByUsername(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

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
                    System.out.println(strResponseBody);
                    return parseJSONuser(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static User parseJSONuser(String response_str) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(response_str);
            jArray = jObject.getJSONArray("user");
            User user = new User();
            JSONObject curr = new JSONObject(jArray.getString(0));
            user.setId(curr.getString("_id"));
            user.setUserName(curr.getString("username"));
            user.setFirstName(curr.getString("first_name"));
            user.setLastName(curr.getString("last_name"));
            user.setPassword(curr.getString("password"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
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

    public static void deleteToAPI(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pacific-tor-50594.herokuapp.com")
                .build();

        final HerokuService service = retrofit.create(HerokuService.class);

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
