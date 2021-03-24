package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Course")
public class Course extends ParseObject {
    public static final String KEY_COURSE_TITLE = "course_title";
    public static final String KEY_FORUM_POSTS = "forum_posts";

    public String getCourseTitle() {
        return getString(KEY_COURSE_TITLE);
    }

    public void setCourseTitle(String title) {
        put(KEY_COURSE_TITLE, title);
    }

    //TODO
    public ArrayList<ForumPost> getForumPosts() {
        return null;
    }

    //TODO
    public void setForumPosts() {
        return;
    }
}

