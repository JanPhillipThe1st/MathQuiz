package com.yamatoapps.mathquiz;

import java.util.ArrayList;
import java.util.Collections;

public class Question {
    public String question = "";
    public String answer = "";

    public String a = "";
    public String b = "";
    public String c = "";
    public String d = "";

    public  String id = "";

    public Question(String question, String answer, String a, String b, String c, String d, String id) {
        this.question = question;
        this.answer = answer;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.id = id;
    }
    public  Question scrambleChoices(){
        //Take all choices and put them in an array
        ArrayList<String> choices = new ArrayList<String>();
        choices.add(this.a);
        choices.add(this.b);
        choices.add(this.c);
        choices.add(this.d);
        //Shuffle the choices
        Collections.shuffle(choices);
        //Now you can reassign the choices
        this.a = choices.get(0);
        this.b = choices.get(1);
        this.c = choices.get(2);
        this.d = choices.get(3);
        return this;
    }
}
