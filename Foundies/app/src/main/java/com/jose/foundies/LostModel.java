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


public class LostModel {

    public void LostItemModel(){}

    public String jsonLostPost(Item f) {
        String jsonPost = "{ \"lost\": { \"_id\": \"" + f.getItemID() + "\", \"category1\":\"" + f.getCategory()
                + "\", \"category2\":\"" + f.getSubcategory() + "\", \"category3\":\""
                + f.getAnswersAsString() + "\", \"username\":\"" + f.getUserID() + "\", \"timestamp\":\""
                + f.getTimestamp() + "\", \"latitude\":\""
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
            Call<ResponseBody> call = service.createLostItem(requestBody);
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
                items.add(item);
            }
            return items;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<Item> parseJSON(String strResponseBodyLost) {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(strResponseBodyLost);
            jArray = jObject.getJSONArray("lost");
            ArrayList<Item> lostItems = new ArrayList<Item>();
            for (int i = 0; i < jArray.length(); ++i) {
                Item lost = new Item();
                JSONObject curr = new JSONObject(jArray.getString(i));
                if (curr.getString("_id").isEmpty()) {
                    return null;
                } else {
                    lost.setItemID(curr.getString("_id"));
                    lost.setCategory(curr.getString("category1"));
                    lost.setSubcategory(curr.getString("category2"));
                    lost.setAnswers(curr.getString("category3"));
                    lost.setLatitude(curr.getString("latitude"));
                    lost.setLongitude(curr.getString("longitude"));
                    lost.setTimestamp(curr.getString("timestamp"));
                    lost.setUserID(curr.getString("username"));
                    lostItems.add(lost);
                }
            }
            return lostItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    public ArrayList<Item> getLostItemWithCategories(Item item) {
        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getLostItemWithCategories(item.getCategory(), item.getSubcategory());
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBodyLost = response.body().string();
                    ArrayList<Item> items = parseJSON(strResponseBodyLost);
                    return findMatches(items, item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Item> findMatches(ArrayList<Item> items, Item itemToMatch) {
        ArrayList<String> answers = itemToMatch.getAnswers();
        for (int i=items.size()-1; i>=0; --i) {
            ArrayList<String> currentAnswers = items.get(i).getAnswers();
            int matches = 0;
            for(int j=0; j<answers.size(); ++j) {
                if(answers.get(j).equals(currentAnswers.get(j)) || answers.get(j).equals("Other") ||
                        currentAnswers.get(j).equals("Other")) {
                    ++matches;
                }
            }
            if ((double)(matches/answers.size()) < 0.75)
                items.remove(i);
        }
        for (int i=0; i<items.size(); ++i) {
            items.get(i).printItem();
        }
        return items;
    }


    public void deleteLostItem(Item item) {

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Call<ResponseBody> call = service.deleteLostItem(item.getItemID());
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
