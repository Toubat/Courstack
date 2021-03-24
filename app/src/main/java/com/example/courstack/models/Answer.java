package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Answer")
public class Answer extends ParseObject {
    public static final String KEY_STUDENT = "student";
    public static final String KEY_ANSWER_TEXT = "answer_text";

    public ParseUser getStudent() {
        return getParseUser(KEY_STUDENT);
    }

    public void setStudent(ParseUser student) {
        put(KEY_STUDENT, student);
    }

    public String getText() {
        return getString(KEY_ANSWER_TEXT);
    }

    public void setText(String text) {
        put(KEY_ANSWER_TEXT, text);
    }

}
