package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Categories extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        final Controller controller = (Controller) getApplicationContext();

        final Button electronic = (Button) findViewById(R.id.elecButton);
        final Button jewelry = (Button) findViewById(R.id.jewelButton);
        final Button personal = (Button) findViewById(R.id.personButton);
        final Button custom = (Button) findViewById(R.id.customButton);

        electronic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String e = "Electronics";
                controller.setCategory(e);
                Intent i = new Intent(getBaseContext(), SubCategories.class);
                i.putExtra("Category", e);
                startActivity(i);
                finish();
            }
        });
        jewelry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String j = "Jewelry";
                controller.setCategory(j);
                Intent i = new Intent(getBaseContext(), SubCategories.class);
                i.putExtra("Category", j);
                startActivity(i);
                finish();
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String p = "Personal Items";
                controller.setCategory(p);
                Intent i = new Intent(getBaseContext(), SubCategories.class);
                i.putExtra("Category", p);
                startActivity(i);
                finish();
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String c = "Custom";
                controller.setCategory(c);
                Intent i = new Intent(getBaseContext(), SubCategories.class);
                i.putExtra("Category", c);
                startActivity(i);
                finish();
            }
        });
    }
}