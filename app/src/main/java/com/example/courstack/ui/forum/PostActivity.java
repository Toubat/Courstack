package com.example.courstack.ui.forum;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courstack.R;
import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.ForumPost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    public static final String TAG = "postActivity";
    ArrayList<AnswerPost> answers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_rv_answers);
        RecyclerView rvAnswers = findViewById(R.id.rvAnswers);


        // Create adapter
        PostAdapter postAdapter = new PostAdapter(this, answers);
    }

    public void queryPost(String id) {
        // Specify which forumPost to query
        ParseQuery<AnswerPost> query = ParseQuery.getQuery(AnswerPost.class);
        query.include(AnswerPost.KEY_STUDENT);
        query.include(AnswerPost.KEY_ANSWER);
        query.include(AnswerPost.KEY_PARENT_FORUM);
        query.include("updatedAt");
        query.include("objectId");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<AnswerPost>() {
            @Override
            public void done(List<AnswerPost> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting answers", e);
                    Toast.makeText(PostActivity.this, "Issue with getting answerPosts!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "All answerPosts");
                    answers.addAll(items);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
