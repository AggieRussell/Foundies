package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LostorFound extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_or_found);

        final Controller controller = (Controller) getApplicationContext();

        final Button found = (Button) findViewById(R.id.found_button);
        final Button lost = (Button) findViewById(R.id.lost_button);

        found.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeFound();
                Intent i = new Intent(getBaseContext(), Qs.class);
                startActivity(i);
                finish();
            }
        });
        lost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setQueryTypeLost();
                Intent i = new Intent(getBaseContext(), Qs.class);
                startActivity(i);
                finish();
            }
        });
    }
}
