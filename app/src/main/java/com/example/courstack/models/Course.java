package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Course")
public class Course extends ParseObject {

    public static final String KEY_COURSE_TITLE = "course_title";
    public static final String KEY_COURSE_NAME = "course_name";
    public static final String KEY_INSTRUCTOR = "instructor";
    public static final String KEY_BACKGROUND = "background";

    public String getCourseTitle() {
        return getString(KEY_COURSE_TITLE);
    }

    public void setCourseTitle(String title) {
        put(KEY_COURSE_TITLE, title);
    }

    public String getCourseName() {
        return getString(KEY_COURSE_NAME);
    }

    public void setCourseName(String name) {
        put(KEY_COURSE_NAME, name);
    }

    public String getInstructor() {
        return getString(KEY_INSTRUCTOR);
    }

    public void setInstructor(String instructor) {
        put(KEY_INSTRUCTOR, instructor);
    }

    public ParseFile getCourseBackground() {
        return getParseFile(KEY_BACKGROUND);
    }

    public void setCourseBackground(ParseFile image) {
        put(KEY_BACKGROUND, image);
    }
}
