package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubCategories extends Activity {

    //Array of images for buttons
    int[] img = {R.drawable.mobile, R.drawable.tablet, R.drawable.laptop, R.drawable.earphones,
            R.drawable.ring, R.drawable.earrings, R.drawable.bracelet, R.drawable.necklace,
            R.drawable.books, R.drawable.pants, R.drawable.wallet, R.drawable.keys,
            R.drawable.mini_phone, R.drawable.mini_phone, R.drawable.mini_phone, R.drawable.mini_phone
    };

    //Array of buttons to display subcategory
    Button[] buttonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategories);

        final Controller controller = (Controller) getApplicationContext();

        //Subcategory Buttons
        final Button one = (Button) findViewById(R.id.buttonOne_A);
        final Button two = (Button) findViewById(R.id.buttonTwo_A);
        final Button three = (Button) findViewById(R.id.buttonThree_A);
        final Button four = (Button) findViewById(R.id.buttonFour_A);

        String category;
        Intent intent = getIntent();
        category = intent.getExtras().getString("Category");

        buttonArray = new Button[]{
                one, two, three, four
        };

        //Category Title
        final TextView title = (TextView) findViewById(R.id.subcat_tv);
        title.setText(category);

        displayImages(category);

        one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AdditionalDetails.class);
                startActivity(i);
                finish();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AdditionalDetails.class);
                startActivity(i);
                finish();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AdditionalDetails.class);
                startActivity(i);
                finish();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AdditionalDetails.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void displayImages(String c){
        int n = 0;
        if(c.equals("")){

        }
        else if (c.equals("Electronics")){
            n = 0;
        }
        else if(c.equals("Jewelry")){
            n = 4;
        }
        else if(c.equals("Personals")){
            n = 8;
        }
        else {
            n = 12;
        }

        buttonArray[0].setBackgroundResource(img[n]);
        buttonArray[1].setBackgroundResource(img[n+1]);
        buttonArray[2].setBackgroundResource(img[n+2]);
        buttonArray[3].setBackgroundResource(img[n+3]);
    }
}