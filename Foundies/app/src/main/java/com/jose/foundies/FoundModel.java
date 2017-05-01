package com.jose.foundies;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                + f.getTimestamp() + "\", \"latitude\":\""
                + f.getLatitude() + "\", \"longitude\":\"" + f.getLongitude() + "\" } }";
        return jsonPost;
    }

//TODO: add null exception handling
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
    public ArrayList<Item> getFoundItemWithCategories(Item item) throws ParseException {
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
        System.out.println("POSTING FOUND ITEM ");
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
                    System.out.println(" !!!THIS IS THE FOUND ITEM RESPONSE: !!!" + response);
                }
            } catch (IOException e) {
                // ...
            }
        }
    }

    public ArrayList<Item> findMatches(ArrayList<Item> items, Item itemToMatch) throws ParseException {
        ArrayList<String> answers = itemToMatch.getAnswers();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateLost = sdf.parse(itemToMatch.getTimestamp());
        System.out.println("DEVLIN: " + itemToMatch.getAnswersAsString());
        for (int i=items.size()-1; i>=0; --i) {
            Date dateCurrentFound = sdf.parse(items.get(i).getTimestamp());
            if (!itemToMatch.getUserID().equals(items.get(i).getUserID())) {
                if (dateLost.after(dateCurrentFound)) {
                    items.remove(i);
                } else {
                    ArrayList<String> currentAnswers = items.get(i).getAnswers();
                    int matches = 0;
                    for (int j = 0; j < answers.size(); ++j) {
                        System.out.println("HERE: " + answers.get(j) + " - " + currentAnswers.get(j));
                        if (answers.get(j).equals(currentAnswers.get(j)) || answers.get(j).equals("Other") ||
                                currentAnswers.get(j).equals("Other")) {
                            ++matches;
                        }
                    }
                    if ((double) (matches / answers.size()) < 0.75)
                        items.remove(i);
                }
            }
            else {
                items.remove(i);
            }
        }
        out.println("\n\nMATCHING ITEMS\n");
        for (int i=0; i<items.size(); ++i) {
            out.println("\n");
            items.get(i).printItem();
        }
        return items;
    }


    public ArrayList<Item> getFoundItemsByUsername(String username){

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getFoundItemsByUser(username);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    String strResponseBody = response.body().string();
                    System.out.println("This is the response body" + strResponseBody);
                    return parseJSONUserFound(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Item> parseJSONUserFound(String response_str) {
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

    public void deleteFoundItem(Item item) {

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Call<ResponseBody> call = service.deleteFoundItem(item.getItemID());
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
