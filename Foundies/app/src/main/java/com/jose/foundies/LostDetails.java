package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class LostDetails extends Activity {

    ArrayList<String> kinds;
    ArrayList<String> names;
    ArrayList<ArrayList<String>> choices;
    LinearLayout linearLayout;
    ArrayList<Object> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_details);

        final Controller controller = (Controller) getApplicationContext();
        kinds = controller.getKinds();
        names = controller.getNames();
        choices = controller.getChoices();

        linearLayout = (LinearLayout) findViewById(R.id.lostDetailsLinearLayout);

        // create the XML objects for each question
        displayQuestions();

        final Button next = (Button) findViewById(R.id.lost_button);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FoundMap.class);
                startActivity(i);
                finish();
            }
        });
    }

    protected void displayQuestions() {
        for(int i=0; i<kinds.size(); ++i) {
            String kind = kinds.get(i);
            switch (kind) {
                case "drop" :
                    createDrop(i);
                    break;
                case "radio" :
                    createRadio(i);
                    break;
                default :
                    break;
            }
        }
    }

    protected void createDrop(int i) {

    }

    protected void createRadio(int i) {
        //RadioGroup rg = new RadioGroup();

    }
}
