package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Controller controller = (Controller) getApplicationContext();

        final Button register = (Button) findViewById(R.id.register_button);
        final Button back = (Button) findViewById(R.id.back_button);
        final EditText fname = (EditText) findViewById(R.id.firstname_field);
        final EditText lname = (EditText) findViewById(R.id.lastname_field);
        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText password = (EditText) findViewById(R.id.password_field);


        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String fnamestr = fname.getText().toString();
                String lnamestr = lname.getText().toString();
                String emailstr = email.getText().toString();
                String passstr = password.getText().toString();

                if (!(controller.isEmptyField(fname, lname, email, password))) {
                    Toast.makeText(getApplicationContext(), "Field Missing!", Toast.LENGTH_SHORT).show();
                } else {

                    //Send details to database
                    String checkEmail = controller.createUser(fnamestr, lnamestr, emailstr, passstr, Utility.getDate(), "0", "0");
                    if (checkEmail == null) {
                        Intent i = new Intent(getBaseContext(), ProfilePage.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), checkEmail, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), LoadScreen.class);
                startActivity(i);
                finish();
            }
        });
    }
}
