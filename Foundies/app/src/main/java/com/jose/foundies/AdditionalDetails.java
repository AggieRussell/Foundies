package com.jose.foundies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
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
                if (checkAllAnswersSelected()) {
                    controller.setAnswers(getAnswers(), getExtraDetails());
                    Intent i = new Intent(getBaseContext(), FoundMap.class);
                    startActivity(i);
                    finish();
                }
                else {
                    String text = "Please select an answer for each question.";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }

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
                case "*drop" :
                    createContingentDrop(i);
                    break;
                case "radio" :
                    createRadio(i);
                    break;
                case "*radio" :
                    createContingentRadio(i);
                    break;
                case "check" :
                    createCheck(i);
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

    protected void createContingentDrop(int i) {
        final LinearLayout ll = new LinearLayout(this);
        ll.setVisibility(GONE);
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // params.gravity = Gravity.CENTER;
        // ll.setLayoutParams(params);
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
        ((CheckBox)questions.get(i-1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((CheckBox)view).isChecked())
                    ll.setVisibility(View.VISIBLE);
                else
                    ll.setVisibility(GONE);
            }
        });
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

    protected void createContingentRadio(int i) {
        final RadioGroup rg = new RadioGroup(this);
        rg.setVisibility(GONE);
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
        ((CheckBox)questions.get(i-1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((CheckBox)view).isChecked())
                    rg.setVisibility(View.VISIBLE);
                else
                    rg.setVisibility(GONE);
            }
        });
        questions.add(rg);
        linearLayout.addView(rg);
    }

    protected void createCheck(int i) {
        CheckBox cb = new CheckBox(this);
        cb.setText(names.get(i)+"? ");
        questions.add(cb);
        linearLayout.addView(cb);
    }

    protected int convertToDps(int x) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (x * scale + 0.5f);
    }

    protected boolean checkAllAnswersSelected() {
        for (int i=0; i<questions.size(); ++i) {
            switch (questions.get(i).getClass().getSimpleName()) {
                case "RadioGroup" :
                    if (((RadioGroup)questions.get(i)).getCheckedRadioButtonId() == -1)
                        return false;
                case "CheckBox" :
                    // If box isn't checked, don't check if next item is checked
                    if (!((CheckBox)questions.get(i)).isChecked())
                        while (kinds.get(i+1).charAt(0) == '*')
                            ++i;
            }
        }
        return true;
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
                case "CheckBox" :
                    CheckBox checkBox = (CheckBox) questions.get(i);
                    if (checkBox.isChecked())
                        answers.add("Yes");
                    else {
                        answers.add("No");
                        while (kinds.get(i+1).charAt(0) == '*') {
                            answers.add("not applicable");
                            ++i;
                        }
                    }
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
