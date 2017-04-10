package com.jose.foundies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.System.out;

public class AdditionalDetails extends Activity {

    ArrayList<String> kinds;
    ArrayList<String> names;
    ArrayList<ArrayList<String>> choices;
    LinearLayout linearLayout;
    ArrayList<Object> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_details);

        final Controller controller = (Controller) getApplicationContext();

        kinds = controller.getKinds();
        names = controller.getNames();
        choices = controller.getChoices();

        linearLayout = (LinearLayout) findViewById(R.id.AddionalDetailsLinearLayout);

        // create the XML objects for each question
        displayQuestions();

        final Button next = (Button) findViewById(R.id.button);
        final Button back = (Button) findViewById(R.id.backButton);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.setAnswers(getAnswers(), getExtraDetails());
                Intent i = new Intent(getBaseContext(), FoundMap.class);
                startActivity(i);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Qs.class);
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
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(this);
        t.setText(names.get(i));
        t.setPadding(convertToDps(10), convertToDps(5), convertToDps(10), convertToDps(5));
        ll.addView(t);
        ArrayList<String> c = choices.get(i);
        Spinner s = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, c);
        s.setAdapter(adapter);
        ll.addView(s);
        questions.add(s);
        linearLayout.addView(ll);
    }

    protected void createRadio(int i) {
        RadioGroup rg = new RadioGroup(this);
        rg.setPadding(convertToDps(10), convertToDps(5), convertToDps(10), convertToDps(5));
        rg.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(this);
        t.setText(names.get(i)+ ":  ");
        rg.addView(t);
        ArrayList<String> c = choices.get(i);
        for (int j=0; j<c.size(); ++j) {
            RadioButton rb = new RadioButton(this);
            rb.setText(c.get(j).replaceAll("_", " "));
            rg.addView(rb);
        }
        questions.add(rg);
        linearLayout.addView(rg);
    }

    protected int convertToDps(int x) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (x * scale + 0.5f);
    }

    protected ArrayList<String> getAnswers() {
        ArrayList<String> answers = new ArrayList<String>();
        out.println("GET_SIMPLE_NAME:");
        for (int i=0; i<questions.size(); ++i) {
            switch (questions.get(i).getClass().getSimpleName()) {
                case "Spinner" :
                    Spinner spinner = (Spinner) questions.get(i);
                    answers.add(spinner.getSelectedItem().toString());
                    break;
                case "RadioGroup" :
                    RadioGroup radioGroup = (RadioGroup) questions.get(i);
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                    answers.add(radioButton.getText().toString());
                    break;
                default:
                    break;
            }
        }
        return answers;
    }

    protected String getExtraDetails() {
        EditText extraDetails = (EditText) findViewById(R.id.extraDetails);
        return extraDetails.getText().toString();
    }
}
