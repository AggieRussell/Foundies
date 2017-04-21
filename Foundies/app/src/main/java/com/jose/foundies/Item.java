package com.jose.foundies;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.lang.System.out;

/**
 * Created by cmrussell on 4/10/17.
 */

public class Item {

    private String itemID;
    private String userID;
    private String category;
    private String subcategory;
    private ArrayList<String> answers = new ArrayList<String>();
    private String extraDetails;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private String timestamp;

    //TODO: add date to the Item class

    public Item() {
        itemID = Utility.uniqueID();
    }

    public String getItemID() { return itemID; }

    public String getUserID() { return userID; }

    public String getCategory() { return category; }

    public String getSubcategory() { return subcategory; }

    public ArrayList<String> getAnswers() { return answers; }

    public String getAnswersAsString() {
        String str = "";
        for (int i=0; i<answers.size(); ++i) {
            str+=answers.get(i).replaceAll(" ", "_");
            if(i<answers.size()-1) {
                str+=",";
            }
        }
        return str;
    }

    public String getExtraDetails() { return extraDetails; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String getTimestamp() { return timestamp; }

    public void setItemID(String itemID) { this.itemID = itemID; }

    public void setUserID(String userID) { this.userID = userID; }

    public void setSelections(String category, String subcategory, String date) {
        this.category = category;
        this.subcategory = subcategory;
        timestamp = date;
    }

    public void setAnswers(ArrayList<String> answers) { this.answers = answers; }

    public void setAnswers(String answers) {
        String[] splitAnswers = answers.split(",");
        ArrayList<String> newAnswers = new ArrayList<String>();
        for(int i=0; i<splitAnswers.length; ++i) {
            newAnswers.add(splitAnswers[i]);
        }
        this.answers = newAnswers;
    }

    public void setExtraDetails(String details) { extraDetails = details;}

    public void setLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) { this.latitude = Double.parseDouble(latitude); }

    public void setLongitude(String longitude) { this.longitude = Double.parseDouble(longitude); }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public void printItem() {
        out.printf("%s\n%s\n%s\n%s\n%s\n%f\n%f\n%s\n",itemID, userID, category, subcategory, getAnswersAsString(), latitude, longitude, timestamp);
    }

    /*----- Not currently being used, but may be used in the future if graphic buttons are used
            instead of spinners -- Only being used by Categories class, which is not being used -----*/

    public void setCategory(String c) {
        category = c;
    }

    public void setSubcategory(String s) {
        subcategory = s;
    }

}
