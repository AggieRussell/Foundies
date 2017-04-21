package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import static java.lang.System.out;

public class Qs extends Activity {

    String cat;
    String subcat;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qs);

        final Controller controller = (Controller) getApplicationContext();

        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerCategory.setBackgroundColor(Color.DKGRAY);
        ArrayList<String> categories = controller.getCategories();
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);

        // set categories in question 1
        if (categories != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
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

        // set calendar to current date and set min (4 weeks earlier) and max dates
        long time = System.currentTimeMillis();
        calendar.setDate(time);
        calendar.setMaxDate(time);
        Calendar minDate = Calendar.getInstance();
        out.println("WEEK OF YEAR");
        out.println(minDate.get(Calendar.WEEK_OF_YEAR));
        minDate.add(Calendar.WEEK_OF_YEAR, -4);
        out.println(minDate.get(Calendar.WEEK_OF_YEAR));
        calendar.setMinDate(minDate.getTimeInMillis());

        final Button next = (Button) findViewById(R.id.next_button);
        final Button back = (Button) findViewById(R.id.backButton);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                date = Integer.toString(year);
                if (month < 9)
                    date += "-0"+Integer.toString(month+1);
                else
                    date += "-"+Integer.toString(month+1)+"-"+Integer.toString(dayOfMonth);
                if (dayOfMonth < 10)
                    date += "-0"+Integer.toString(dayOfMonth);
                else
                    date += "-"+Integer.toString(dayOfMonth);
                out.println("SELECTED_DATE: " + date);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.sendSelections(cat,subcat,date);
                Intent i = new Intent(getBaseContext(), AdditionalDetails.class);
                startActivity(i);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ProfilePage.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setSubcategories(ArrayList<String> subcategories) {
        // set subcategories based on choice in question 1
        Spinner spinnerSubcategory = (Spinner) findViewById(R.id.spinnerSubcategory);
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
