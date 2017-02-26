package com.jose.foundies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LostorFound extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostor_found);

        final Button found = (Button) findViewById(R.id.found_button);
        final Button lost = (Button) findViewById(R.id.lost_button);

        found.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FoundQs.class);
                startActivity(i);
                finish();
            }
        });
        lost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), LostQs.class);
                startActivity(i);
                finish();
            }
        });
    }
}
