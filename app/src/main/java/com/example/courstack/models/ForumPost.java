package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("ForumPost")
public class ForumPost extends ParseObject {
    public static final String KEY_TITLE = "title";
    public static final String KEY_COURSE = "course";
    public static final String KEY_ASKER = "asker";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_ANSWERPOSTS = "answer_posts";

    public String getTitile() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getCourse() {
        return getString(KEY_COURSE);
    }

    public void setCourse(String course) {
        put(KEY_COURSE, course);
    }

    public ParseUser getAsker() {
        return getParseUser(KEY_ASKER);
    }

    public void setAsker(ParseUser asker) {
        put(KEY_ASKER, asker);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    //TODO
    public ArrayList<AnswerPost> getAnswerPosts() {
        return null;
    }

    //TODO
    public void setAnswerposts() {
        return;
    }


}
