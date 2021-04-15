package com.example.courstack;

import android.app.Application;

import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.Course;
import com.example.courstack.models.ForumPost;
import com.example.courstack.models.VideoPost;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(Answer.class);
        ParseObject.registerSubclass(AnswerPost.class);
        ParseObject.registerSubclass(ForumPost.class);
        ParseObject.registerSubclass(VideoPost.class);
        ParseObject.registerSubclass(Course.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("h8yUjNaHoahgvreSdqeLtlYWEwwdMpEQoZkkanVz")
                .clientKey("S2lr1qlB0mY8G1pGovsxJ0dUuVPkv0ZfVezEBacu")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
