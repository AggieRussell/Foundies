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

import static java.lang.System.out;

/**
 * Created by kylepreston on 3/29/17.
 */

public class FoundModel {

    private String strResponseBody = "";

    public FoundModel() {
    }

    public String jsonFoundPost(Item f) {
        String jsonPost = "{ \"found\": { \"_id\": \"" + f.getItemID() + "\", \"category1\":\"" + f.getCategory()
                + "\", \"category2\":\"" + f.getSubcategory() + "\", \"category3\":\""
                + f.getAnswersAsString() + "\", \"username\":\"" + f.getUserID() + "\", \"timestamp\":\""
                + Utility.getDate() + "\", \"latitude\":\""
                + f.getLatitude() + "\", \"longitude\":\"" + f.getLongitude() + "\" } }";
        return jsonPost;
    }


    public ArrayList<Item> parseJSON() {
        JSONObject jObject;
        JSONArray jArray;
        try {
            jObject = new JSONObject(strResponseBody);
            jArray = jObject.getJSONArray("found");
            ArrayList<Item> foundItems = new ArrayList<Item>();
            for (int i = 0; i < jArray.length(); ++i) {
                Item found = new Item();
                JSONObject curr = new JSONObject(jArray.getString(i));
                if (curr.getString("_id").isEmpty()) {
                    return null;
                } else {
                    found.setItemID(curr.getString("_id"));
                    found.setCategory(curr.getString("category1"));
                    found.setSubcategory(curr.getString("category2"));
                    found.setAnswers(curr.getString("category3"));
                    found.setLatitude(curr.getString("latitude"));
                    found.setLongitude(curr.getString("longitude"));
                    found.setTimestamp(curr.getString("timestamp"));
                    found.setUserID(curr.getString("username"));
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
    public ArrayList<Item> getAllFoundItems() {
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
                    strResponseBody = response.body().string();
                    return parseJSON();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // use to get found items in matching Category and Subcategory
    public ArrayList<Item> getFoundItemWithCategories(Item item) {
        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getFoundItemWithCategories(item.getCategory(), item.getSubcategory());
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    strResponseBody = response.body().string();
                    out.println("    RESPONSE FOR getFoundItemWithCategories:" + strResponseBody);
                    return findMatches(parseJSON(), item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void postToFound(String jsonPost) {
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
        out.println("\n\nMATCHING ITEMS\n");
        for (int i=0; i<items.size(); ++i) {
            out.println("\n");
            items.get(i).printItem();
        }
        return items;
    }
}
