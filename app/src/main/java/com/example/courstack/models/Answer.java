package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Answer")
public class Answer extends ParseObject {

    public static final String KEY_STUDENT = "student";
    public static final String KEY_ANSWER_TEXT = "answer_text";
    public static final String KEY_PARENT = "parent";
    public static final String KEY_TITLE = "answer_title";

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setKeyTitle(String title) {
        put(KEY_TITLE, title);
    }

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

    public void setParent(AnswerPost answerPost) {
        put(KEY_PARENT, answerPost);
    }

    public AnswerPost getParentAnswerPost() {
        return (AnswerPost) getParseObject(KEY_PARENT);
    }
}
