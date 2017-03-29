package com.jose.foundies;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    DatabaseHelper helper = new DatabaseHelper(this);

    UserController controller = new UserController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UserModel.getFoundItems();




        final Button register = (Button) findViewById(R.id.register_button);
        final EditText fname = (EditText) findViewById(R.id.firstname_field);
        final EditText lname = (EditText) findViewById(R.id.lastname_field);
        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText password = (EditText) findViewById(R.id.password_field);



        register.setOnClickListener(new View.OnClickListener() {
            private boolean isEmptyField(EditText fname, EditText lname, EditText email, EditText pass) {
                if (fname.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "First name missing!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if (lname.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Last name missing!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if (email.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Email address missing!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if (pass.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Password missing!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    return true;
                }
            }

            public void onClick(View v) {

                String fnamestr = fname.getText().toString();
                String lnamestr = lname.getText().toString();
                String emailstr = email.getText().toString();
                String passstr = password.getText().toString();

                if (!isEmptyField(fname, lname, email, password)) {
                // Fill all
                }
                else {

                    //Send details to database
                    controller.createUser(fnamestr, lnamestr, emailstr, passstr);

                    //Go to lost or found
                    Intent i = new Intent(getBaseContext(), LostorFound.class);
                    startActivity(i);
                    finish();

                }
            }

        });
    }
}
