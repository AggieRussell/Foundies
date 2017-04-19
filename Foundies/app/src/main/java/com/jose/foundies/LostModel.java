package com.jose.foundies;

import android.os.StrictMode;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.System.out;
import java.io.IOException;
import java.util.ArrayList;

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
                if (curr.getString("_id").isEmpty()) {
                    return null;
                } else {
                    item.setItemID(curr.getString("_id"));
                    item.setUserID(curr.getString("username"));
                    item.setCategory(curr.getString("category1"));
                    item.setSubcategory(curr.getString("category2"));
                    item.setAnswers(curr.getString("category3"));
                    item.setLatitude(curr.getString("latitude"));
                    item.setLongitude(curr.getString("longitude"));
                    item.setTimestamp(curr.getString("timestamp"));
                    items.add(item);
                }
            }
            return items;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

}
