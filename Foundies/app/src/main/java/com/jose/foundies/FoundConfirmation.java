package com.jose.foundies;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
            datum.put("Question", controller.getQuestions().get(i).getName());
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
                Toast toast = Toast.makeText(getApplicationContext(), "POSTED TO DATABASE", Toast.LENGTH_SHORT);
                //TODO: Get rid of print statements
                System.out.println("This is the user: " + controller.getUserId());
                System.out.println("This is the lat: " + controller.getLatitude().toString());
                System.out.println("This is the long: " + controller.getLongitude().toString());
                System.out.println("This is the answers: " + controller.getAnswersString());
                System.out.println("This is the category: " + controller.getCategory());
                System.out.println("This is the suncategory: " + controller.getSubcategory());
                System.out.println("This is the time: " + Utility.getDate());
                controller.postFoundItem();
                toast.show();
                Intent lostOrFound = new Intent(getBaseContext(), LostorFound.class);
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
