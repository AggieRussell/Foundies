package com.jose.foundies;

import java.util.ArrayList;

/**
 * Created by christine on 4/4/17.
 */

public class Question {
    private String q1;
    private String q2;
    private String kind;
    private String name;
    private ArrayList<String> choices;

    public Question() {
        q1 = "NOT_DEFINED";
        q2 = "NOT_DEFINED";
        kind = "NOT_DEFINED";
        name = "NOT_DEFINED";
        choices = new ArrayList<String>();
    }

    public Question(String q1, String q2, String kind, String name, ArrayList<String> choices) {
        this.q1 = q1;
        this.q2 = q2;
        this.kind = kind;
        this.name = name;
        this.choices = choices;
    }

    public String getQ1() {
        return q1;
    }

    public String getQ2() {
        return q2;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }
}
