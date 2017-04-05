package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class LostQs extends Activity {

    String cat;
    String subcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_qs);

        final Controller controller = (Controller) getApplicationContext();

        Spinner spinnerCategory = (Spinner) findViewById(R.id.lostCategory);
        spinnerCategory.setBackgroundColor(Color.DKGRAY);
        ArrayList<String> categories = controller.getCategories();
        // set categories in question 1
        if (categories != null) {
            String[] c = categories.toArray(new String[categories.size()]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, c);
            spinnerCategory.setAdapter(adapter);
        }
        // find which category was selected
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = parent.getItemAtPosition(position).toString();
                setSubcategories(controller.getSubcategories(cat));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        final Button next = (Button) findViewById(R.id.next_button);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.sendSelections(cat,subcat);
                Intent i = new Intent(getBaseContext(), LostDetails.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void setSubcategories(ArrayList<String> subcategories) {
        // set subcategories based on choice in question 1
        Spinner spinnerSubcategory = (Spinner) findViewById(R.id.lostSubcategories);
        spinnerSubcategory.setBackgroundColor(Color.DKGRAY);
        if (subcategories != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subcategories);
            spinnerSubcategory.setAdapter(adapter);
        }
        // find which subcategory was selected
        spinnerSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }
}
