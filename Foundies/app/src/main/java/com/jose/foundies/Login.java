package com.jose.foundies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        final Button login = (Button) findViewById(R.id.login_button);
        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText password = (EditText) findViewById(R.id.password_field);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(view.getId()== R.id.login_button) {
                    String emailstr = email.getText().toString();
                    String passstr = password.getText().toString();


                    String passCheck = helper.searchPass(emailstr);
                    if (passstr.equals(passCheck)) {
                        Intent i = new Intent(getBaseContext(), LostorFound.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email and Password don't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
