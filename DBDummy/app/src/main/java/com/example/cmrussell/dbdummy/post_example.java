package com.example.cmrussell.dbdummy;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class post_example extends AppCompatActivity {

    private TextView mTextMessage;

/*
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://pacific-tor-50594.herokuapp.com")
            .build();

    final HerokuService service = retrofit.create(HerokuService.class);
*/

    Button button1;
    EditText userName, firstName, lastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_example);

        userName = (EditText) findViewById(R.id.username);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameStr = userName.getText().toString();
                String lastNameStr = lastName.getText().toString();
                String firstNameStr = firstName.getText().toString();
                //Utility Class is a helper class creating the json text
                String strRequestBody = Utility.jsonUserPost(userNameStr, firstNameStr, lastNameStr);
                Utility.postToAPI(strRequestBody);
                Intent mainActivity = new Intent(post_example.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    private void button1Click()
    {
        startActivity(new Intent(".MainActivity"));
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                button1Click();
                break;
    }
    }
}
