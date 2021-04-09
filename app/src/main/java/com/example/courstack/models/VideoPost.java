package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;

@ParseClassName("VideoPost")
public class VideoPost extends ParseObject {

    public static final String KEY_VIDEO_FILE = "video_file";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ANSWERPOSTS = "answerPosts";
    public static final String KEY_FRONT_IMAGE = "front_image";

    public ParseFile getFrontImage() {
        return getParseFile(KEY_FRONT_IMAGE);
    }

    public void setFrontImage(ParseFile imageFile) {
        put(KEY_FRONT_IMAGE, imageFile);
    }

    public ParseFile getVideoFile() {
        return getParseFile(KEY_VIDEO_FILE);
    }

    public void setVideoFile(ParseFile videoFile) {
        put(KEY_VIDEO_FILE, videoFile);
    }

    public ParseUser getStudent() {
        return getParseUser(KEY_STUDENT);
    }

    public void setStudent(ParseUser student) {
        put(KEY_STUDENT, student);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    //TODO
    public ArrayList<AnswerPost> getAnswerPosts() {
        return null;
    }

    //TODO
    public void setAnswerPosts() {
        return;
    }
}
