package com.jose.foundies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_details);

        controller = (Controller) getApplicationContext();

        kinds = controller.getKinds();
        names = controller.getNames();
        choices = controller.getChoices();

        linearLayout = (LinearLayout) findViewById(R.id.AddionalDetailsLinearLayout);

        // create the XML objects for each question
        displayQuestions();

        final Button next = (Button) findViewById(R.id.button);
        final Button back = (Button) findViewById(R.id.backButton);
        final EditText extraDetails = (EditText) findViewById(R.id.extraDetails);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (checkAllAnswersSelected()) {
                    controller.setAnswers(getAnswers(), getExtraDetails());
                    Intent i = new Intent(getBaseContext(), FoundMap.class);
                    startActivity(i);
                    finish();
                }
                else {
                    String text = "Please select an answer\nfor each question.";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
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

        extraDetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                case "^drop" :
                    createSubsequentDrop(i);
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
        // TODO: The stuff below doesn't work.  Might try again later...
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

    protected void createSubsequentDrop(int i) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(this);
        t.setText(names.get(i));
        t.setPadding(convertToDps(10), convertToDps(5), convertToDps(10), convertToDps(5));
        ll.addView(t);
        Spinner s = new Spinner(this);
        ll.addView(s);
        final int index = i;
        final ArrayList<ArrayList<String>> subsequentChoices = controller.getSubsequentChoices(i);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subsequentChoices.get(0));
        s.setAdapter(adapter);
        questions.add(s);
        linearLayout.addView(ll);
        ((Spinner)questions.get(i-1)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> currSubsequentChoices = subsequentChoices.get(position);
                createSubsequentDropHelper(index,currSubsequentChoices);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    protected void createSubsequentDropHelper(int i, ArrayList<String> subsequentChoices) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subsequentChoices);
        ((Spinner)questions.get(i)).setAdapter(adapter);
    }

    protected void createRadio(int i) {
        HorizontalScrollView scrollView = new HorizontalScrollView(this);
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
        scrollView.addView(rg);
        linearLayout.addView(scrollView);
    }

    protected void createContingentRadio(int i) {
        HorizontalScrollView scrollView = new HorizontalScrollView(this);
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
        scrollView.addView(rg);
        linearLayout.addView(scrollView);
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
                    break;
                case "CheckBox" :
                    // If box isn't checked, don't check if next item is checked
                    if (!(((CheckBox)questions.get(i)).isChecked()))
                        while ((i+1)<kinds.size() && kinds.get(i+1).charAt(0) == '*')
                            ++i;
                    break;
                default:
                    break;
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
                    answers.add(spinner.getSelectedItem().toString().replaceAll(" ", "_"));
                    break;
                case "RadioGroup" :
                    RadioGroup radioGroup = (RadioGroup) questions.get(i);
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                    answers.add(radioButton.getText().toString().replaceAll(" ", "_"));
                    break;
                case "CheckBox" :
                    CheckBox checkBox = (CheckBox) questions.get(i);
                    if (checkBox.isChecked())
                        answers.add("Yes");
                    else {
                        answers.add("No");
                        while ((i+1)<kinds.size() && kinds.get(i+1).charAt(0) == '*') {
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
