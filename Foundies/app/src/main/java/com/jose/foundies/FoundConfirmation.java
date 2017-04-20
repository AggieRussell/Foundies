package com.jose.foundies;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FoundConfirmation extends AppCompatActivity {

    public static boolean isLost() {
        return isLost;
    }

    public static void setIsLost(boolean isLost) {
        FoundConfirmation.isLost = isLost;
    }

    private static boolean isLost = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_confirmation);
        final Controller controller = (Controller) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<String> allCategories = new ArrayList<>();
        for(String item : controller.getCategories()){
            allCategories.addAll(controller.getSubcategories(item));
        }

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String,String> datum;
        for (int i = 0; i < controller.getAnswers().size(); ++i){
            datum = new HashMap<String,String>(2);
            datum.put("Question", controller.getNames().get(i));
            datum.put("Answer", controller.getAnswers().get(i));
            data.add(datum);
        }

        LatLng selectedLocation = new LatLng(controller.getLatitude(), controller.getLongitude());
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(controller.getLatitude(), controller.getLongitude(), 1);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        datum = new HashMap<String,String>(2);
        datum.put("Question", "Address");
        datum.put("Answer", addresses.get(0).getAddressLine(0) + " " +
                addresses.get(0).getLocality() + ", " +
                addresses.get(0).getAdminArea());
        data.add(datum);

        SimpleAdapter listAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                new String[]{"Question", "Answer"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        ListView confirmList = (ListView) findViewById(R.id.confirmList);
        confirmList.setAdapter(listAdapter);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button backButton = (Button) findViewById(R.id.backButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = null;
                if(isLost){
                    toast = Toast.makeText(getApplicationContext(), "POSTED TO LOST DATABASE", Toast.LENGTH_SHORT);
                    controller.postLostItem();
                }
                else {
                    toast = Toast.makeText(getApplicationContext(), "POSTED TO FOUND DATABASE", Toast.LENGTH_SHORT);
                    controller.postFoundItem();
                }
                toast.show();
                Intent lostOrFound = new Intent(getBaseContext(), ProfilePage.class);
                startActivity(lostOrFound);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FoundMap.class);
                startActivity(i);
                finish();
            }
        });

    }

}
