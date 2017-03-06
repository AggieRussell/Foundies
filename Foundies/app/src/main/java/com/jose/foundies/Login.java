package com.jose.foundies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
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
        final Button view = (Button) findViewById(R.id.view_button);
        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText password = (EditText) findViewById(R.id.password_field);




        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Cursor results = helper.getData();
                if (results.getCount()==0){
                    //show message
                    showMessage("Error" , "Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(results.moveToNext()){
                    buffer.append("Id: " + results.getString(0) + "\n");
                    buffer.append("First Name: " + results.getString(1) + "\n");
                    buffer.append("Last Name: " + results.getString(2) + "\n");
                    buffer.append("Email: " + results.getString(3) + "\n");
                    buffer.append("Password: " + results.getString(4) + "\n\n");

                }

                //Show all data

                showMessage("Data", buffer.toString());
            }
        });

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


    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
