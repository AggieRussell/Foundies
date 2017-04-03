package com.example.cmrussell.dbdummy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class FoundItems extends AppCompatActivity {

    TextView foundItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_items);

        foundItem = (TextView) findViewById(R.id.foundView);

        User user = Utility.getUserByUsername("lyle");
        try {
            //for(int i = 0; i < user.size(); i++) {
            foundItem.append("Id: " + user.getId() + "\n");
            foundItem.append("first name: " + user.getFirstName() + "\n");
            foundItem.append("last name: " + user.getLastName() + "\n");
            foundItem.append("username: " + user.getUserName() + "\n");
            foundItem.append("password: " + user.getPassword() + "\n");
                //foundItem.append("Lng: " + found.get(i).getLng() + "\n");
            //}
        }catch(Exception e){

        }
    }
}
