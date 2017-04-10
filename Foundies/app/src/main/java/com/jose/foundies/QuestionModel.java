package com.jose.foundies;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.System.out;

/**
 * Created by christine on 4/4/17.
 */

public class QuestionModel {

    private String strResponseBody = "";
    private ArrayList<Question> questions = new ArrayList<Question>();
    private String selectedCategory;
    private String selectedSubcategory;
    private ArrayList<String> selectedAnswers = new ArrayList<String>();
    private String extraDetails;
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    public ArrayList<Question> getQs(){ return questions; }

    public void getQuestions() {

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getQuestions();
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    strResponseBody = response.body().string();
                    parseJSON();
                    out.println("RESPONSE FROM SERVER: ");
                    out.println(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getQuestionsWithQs(String q1, String q2) {

        final HerokuService service = Utility.connectAPI();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<ResponseBody> call = service.getQuestionsWithQs(q1, q2);
            try {
                Response<ResponseBody> response = call.execute();
                if (response.isSuccessful()) {
                    strResponseBody = response.body().string();
                    parseJSON();
                    out.println("RESPONSE FROM SERVER: ");
                    out.println(strResponseBody);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseJSON() {
        out.println("Starting parse JSON...");
        questions.clear();
        try {
            JSONObject jObject = new JSONObject(strResponseBody);
            JSONArray jArray = jObject.getJSONArray("questions");
            for(int i=0; i<jArray.length(); ++i) {
                out.println(jArray.getString(i));
                JSONObject curr = new JSONObject(jArray.getString(i));
                String q1 = curr.getString("q1");
                String q2 = curr.getString("q2");
                String q3 = curr.getString("q3");
                String[] splitQ3 = q3.split(" - ");
                for (int j=0; j< splitQ3.length; ++j) {
                    String[] splitQ3items = splitQ3[j].split(" ");
                    String kind = splitQ3items[0];
                    String name = splitQ3items[1];
                    name = name.replaceAll("_", " ");
                    ArrayList<String> choices = new ArrayList<String>();
                    for (int k = 2; k< splitQ3items.length; ++k) {
                        choices.add(splitQ3items[k].replaceAll("_", " "));
                    }
                    Question newQ = new Question(q1, q2, kind, name, choices);
                    questions.add(newQ);
                }
            }
        } //end try
        catch (JSONException e) {
            e.printStackTrace();
        }
        out.println("Ending parse JSON...");
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> q1s = new ArrayList<String>();
        for (int i=0; i<questions.size(); ++i) {
            String q1 = questions.get(i).getQ1();
            if (!q1s.contains(q1))
                q1s.add(q1);
        }
        return q1s;
    }

    public ArrayList<String> getSubcategories(String q1) {
        ArrayList<String> q2s = new ArrayList<String>();
        for (int i=0; i<questions.size(); ++i) {
            Question curr = questions.get(i);
            if (curr.getQ1().equals(q1) && !q2s.contains(curr.getQ2())) {
                q2s.add(curr.getQ2());
            }
        }
        return q2s;
    }

    public ArrayList<String> getKinds() {
        ArrayList<String> q3kinds = new ArrayList<String>();
        for (int i=0; i<questions.size(); ++i) {
            Question curr = questions.get(i);
            if (curr.getQ1().equals(selectedCategory) && curr.getQ2().equals(selectedSubcategory)) {
                q3kinds.add(curr.getKind());
            }
        }
        return q3kinds;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> q3names = new ArrayList<String>();
        for (int i=0; i<questions.size(); ++i) {
            Question curr = questions.get(i);
            if (curr.getQ1().equals(selectedCategory) && curr.getQ2().equals(selectedSubcategory)) {
                q3names.add(curr.getName());
            }
        }
        return q3names;
    }

    public ArrayList<ArrayList<String>> getChoices() {
        ArrayList<ArrayList<String>> q3choices = new ArrayList<ArrayList<String>>();
        for (int i=0; i<questions.size(); ++i) {
            Question curr = questions.get(i);
            if (curr.getQ1().equals(selectedCategory) && curr.getQ2().equals(selectedSubcategory)) {
                q3choices.add(curr.getChoices());
            }
        }
        return q3choices;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public ArrayList<String> getSelectedAnswers() { return selectedAnswers; }

    public void setSelections(String category, String subcategory) {
        selectedCategory = category;
        selectedSubcategory = subcategory;
    }

    public void setCategory(String c) {
        selectedCategory = c;
    }

    public void setSubcategory(String s) {
        selectedSubcategory = s;
    }

    public void setAnswers(ArrayList<String> answers) { selectedAnswers = answers; }

    public void setLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
