package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("AnswerPost")
public class AnswerPost extends ParseObject {
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_ANSWERS = "answers";

    public ParseObject getAnswer() {
        return getParseObject(KEY_ANSWER);
    }

    public void setAnswer(ParseObject answer) {
        put(KEY_ANSWER, answer);
    }

    //TODO
    public ArrayList<Answer> getAnswers() {
        return null;
    }

    //TODO
    public void setAnswers(){
        return;
    }

}
