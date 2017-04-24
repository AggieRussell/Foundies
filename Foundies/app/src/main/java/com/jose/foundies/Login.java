package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Controller controller = (Controller) getApplicationContext();
        final Button login = (Button) findViewById(R.id.login_button);
        final Button back = (Button) findViewById(R.id.back_button);

        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText password = (EditText) findViewById(R.id.password_field);

        password.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String check = controller.checkCredentials(email, password);
                            if (check != null) {
                                Toast.makeText(getApplicationContext(), check, Toast.LENGTH_SHORT).show();
                            } else {
                                controller.setCurrentUser(email.getText().toString());
                                controller.updateLastAccessed();
                                controller.setUserID(email.getText().toString());
                                controller.updateLastAccessed();
                                Intent i = new Intent(getBaseContext(), ProfilePage.class);
                                startActivity(i);
                                finish();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(view.getId()== R.id.login_button) {

                    String check = controller.checkCredentials(email, password);
                    if (check != null) {
                        Toast.makeText(getApplicationContext(), check, Toast.LENGTH_SHORT).show();
                    } else {
                        controller.setCurrentUser(email.getText().toString());
                        controller.updateLastAccessed();
                        controller.setUserID(email.getText().toString());
                        controller.updateLastAccessed();
                        Intent i = new Intent(getBaseContext(), ProfilePage.class);
                        startActivity(i);
                        finish();
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

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}

