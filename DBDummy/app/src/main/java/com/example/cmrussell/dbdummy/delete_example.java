package com.example.cmrussell.dbdummy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class delete_example extends AppCompatActivity {


    Button buttonDelete;
    EditText userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_example);

        userName = (EditText) findViewById(R.id.usernameDelete);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameStr = userName.getText().toString();
                //Utility Class is a helper class creating the json text
                Utility.deleteToAPI(userNameStr);
                Intent mainActivity = new Intent(delete_example.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    private void buttonDeleteClick()
    {
        startActivity(new Intent(".MainActivity"));
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDelete:
                buttonDeleteClick();
                break;
        }

    }

}
