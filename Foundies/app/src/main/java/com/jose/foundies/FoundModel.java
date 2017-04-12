package com.jose.foundies;

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
import retrofit2.Response;

/**
 * Created by kylepreston on 3/29/17.
 */

public class FoundModel {
    public FoundModel() {
    }

    public static String jsonFoundPost(Item f) {
        //TODO: Change F to item
        String jsonPost = "{ \"found\": { \"_id\": \"" + f.getItemID() + "\", \"category1\":\"" + f.getCategory()
                + "\", \"category2\":\"" + f.getSubcategory() + "\", \"category3\":\""
                + f.getAnswersAsString() + "\", \"username\":\"" + f.getUserID() + "\", \"timestamp\":\""
                + Utility.getDate() + "\", \"latitude\":\""
                + f.getLatitude() + "\", \"longitude\":\"" + f.getLongitude() + "\" } }";
        return jsonPost;
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
                if (curr.getString("_id").isEmpty()) {
                    return null;
                } else {
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
            }
            return foundItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    //Use to get found items from the database, Lat and Lng for Jason
    public static ArrayList<FoundItem> getFoundItems() {
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
                    return parseJSON(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void postToFound(String jsonPost) {
        final HerokuService service = Utility.connectAPI();

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
}
