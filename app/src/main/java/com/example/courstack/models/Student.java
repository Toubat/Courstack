package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("_User")
public class Student extends ParseUser {

    public Student(){
    }

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAILVERIFIED = "emailVerified";
    public static final String KEY_DESCRIPTION = "self_description";
    public static final String KEY_MAJOR = "major";
    public static final String KEY_IMAGE = "profile_image";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public String getPassword() {
        return getString(KEY_PASSWORD);
    }

    public void setPassword(String encryptedPassword) {
        put(KEY_PASSWORD, encryptedPassword);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getMajor() {
        return getString(KEY_MAJOR);
    }

    public void setMajor(String major) {
        put(KEY_MAJOR, major);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

}

