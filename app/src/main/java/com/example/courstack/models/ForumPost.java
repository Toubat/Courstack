package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("ForumPost")
public class ForumPost extends ParseObject {
    public static final String KEY_TITLE = "title";
    public static final String KEY_COURSE = "course";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getCourse() {
        return getString(KEY_COURSE);
    }

    public void setCourse(String course) {
        put(KEY_COURSE, course);
    }

    public ParseUser getStudent() {
        return getParseUser(KEY_STUDENT);
    }

    public void setStudent(ParseUser student) {
        put(KEY_STUDENT, student);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }


}
