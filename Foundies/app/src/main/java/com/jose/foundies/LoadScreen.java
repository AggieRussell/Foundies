package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoadScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        final Controller controller = (Controller) getApplicationContext();

        final Button join = (Button) findViewById(R.id.join_button);
        final Button signin = (Button) findViewById(R.id.signin_tv);

        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Register.class);
                startActivity(i);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Login.class);
                startActivity(i);
                finish();
            }
        });
    }
}
