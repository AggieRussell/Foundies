package com.jose.foundies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FoundQs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_qs);

        final Button next = (Button) findViewById(R.id.next_button);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FoundDetails.class);
                startActivity(i);
                finish();
            }
        });

    }
}
