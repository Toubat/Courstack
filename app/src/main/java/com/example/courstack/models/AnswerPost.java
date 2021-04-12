package com.example.courstack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("AnswerPost")
public class AnswerPost extends ParseObject {

    public static final String KEY_ANSWER = "answer";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_PARENT_FORUM = "parent_forum";
    public static final String KEY_PARENT_VIDEO = "parent_video";

    public ParseUser getStudent() {
        return getParseUser(KEY_STUDENT);
    }

    public void setAnswerAndStudent(Answer answer) {
        put(KEY_ANSWER, answer);
        put(KEY_STUDENT, answer.getStudent());
    }

    public Answer getAnswer() {
        return (Answer) getParseObject(KEY_ANSWER);
    }

    public ForumPost getParentForumPost() {
        return (ForumPost) getParseObject(KEY_PARENT_FORUM);
    }

    public void setParentForumPost(ForumPost forumPost) {
        put(KEY_PARENT_FORUM, forumPost);
    }

    public void setParentVideoPost(VideoPost videoPost) {
        put(KEY_PARENT_VIDEO, videoPost);
    }
}
