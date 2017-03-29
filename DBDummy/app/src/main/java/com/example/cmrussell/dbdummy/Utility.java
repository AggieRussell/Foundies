package com.example.cmrussell.dbdummy;

import android.content.Intent;
import android.os.StrictMode;

import java.io.IOException;
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

    private static String uniqueId(){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    public static String jsonUserPost(String username, String firstName, String lastName){
        String jsonPost  = "{ \"user\": { \"_id\": \"" + uniqueId() + "\", \"username\":\"" + username + "\", \"first_name\":\"" + firstName + "\", \"last_name\":\"" + lastName + "\" } }";
        System.out.println("\n \n \n " + uniqueId() + "\n \n ");
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
