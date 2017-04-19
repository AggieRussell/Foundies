package com.jose.foundies;

import android.os.StrictMode;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.System.out;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by kylepreston on 3/27/17.
 */

public class LostModel {



    public void LostItemModel(){}

    public String jsonLostPost(Item f) {
        String jsonPost = "{ \"lost\": { \"_id\": \"" + f.getItemID() + "\", \"category1\":\"" + f.getCategory()
                + "\", \"category2\":\"" + f.getSubcategory() + "\", \"category3\":\""
                + f.getAnswersAsString() + "\", \"username\":\"" + f.getUserID() + "\", \"timestamp\":\""
                + Utility.getDate() + "\", \"latitude\":\""
                + f.getLatitude() + "\", \"longitude\":\"" + f.getLongitude() + "\" } }";
        return jsonPost;
    }

    public void postToLost(String jsonPost) {
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

    public ArrayList<Item> getLostItemsByUsername(String username){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getLostItemsByUser(username);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                    System.out.println("This is the response body" + strResponseBody);
                    return parseJSONUserLost(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Item> parseJSONUserLost(String response_str) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(response_str);
            jArray = jObject.getJSONArray("items");
            ArrayList<Item> items = new ArrayList<Item>();
            if(jArray.isNull(0)){
                return null;
            }
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject curr = new JSONObject(jArray.getString(i));
                Item item = new Item();
                item.setItemID(curr.getString("_id"));
                item.setUserID(curr.getString("username"));
                item.setCategory(curr.getString("category1"));
                item.setSubcategory(curr.getString("category2"));
                item.setAnswers(curr.getString("category3"));
                item.setLatitude(curr.getString("latitude"));
                item.setLongitude(curr.getString("longitude"));
                item.setTimestamp(curr.getString("timestamp"));
                System.out.println("This is item " + i + " . With _id: " + item.getItemID());
                items.add(item);
            }
            System.out.println("This is the items arrayList: " + items.get(0).getCategory() + "-- \n this is the other -- " + items.get(1).getCategory());
            return items;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
