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
    UserController controller = new UserController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        final Button login = (Button) findViewById(R.id.login_button);
        final Button view = (Button) findViewById(R.id.view_button);
        final Button delete = (Button) findViewById(R.id.delete_button);
        final Button update = (Button) findViewById(R.id.update_button);


        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText password = (EditText) findViewById(R.id.password_field);




        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Cursor results = helper.getData();

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

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Integer deleteRow = helper.deleteData(email.getText().toString());
                if (deleteRow > 0 )
                    Toast.makeText(getApplicationContext(), "Data Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Data not deleted", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            Contact c = new Contact();
            public void onClick(View view) {
                Contact c = new Contact();
                c.setEmail(email.getText().toString());
                c.setPassword(password.getText().toString());
                helper.updateData(c);
                Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            if(view.getId()== R.id.login_button) {

                String check = controller.checkCredentials(email, password);
                if (check != null) {
                    Toast.makeText(getApplicationContext(), check, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getBaseContext(), LostorFound.class);
                    startActivity(i);
                    finish();
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
