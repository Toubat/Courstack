package com.example.courstack.ui.forum;

import android.content.Intent;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    public static final String TAG = "postActivity";
    ArrayList<AnswerPost> answers;
<<<<<<< HEAD
    AnswerPost mainPost;
=======
    PostAdapter adapter;

>>>>>>> 6e4bd338880e1265ed02a9ac9136c4a05a9aa514
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_rv_answers);
        RecyclerView rvAnswers = findViewById(R.id.rvAnswers);

        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");

        // Create adapter
        PostAdapter postAdapter = new PostAdapter(this, answers);
    }

    public void queryAnswerPosts(String ForumPostId) {
        // Specify which forumPost to query
        ParseQuery<AnswerPost> query = ParseQuery.getQuery(AnswerPost.class);
        query.include(AnswerPost.KEY_STUDENT);
        query.include(AnswerPost.KEY_ANSWER);
        query.include(AnswerPost.KEY_PARENT_FORUM);
        query.include("objectId");
        query.include("updatedAt");
        query.whereEqualTo(AnswerPost.KEY_PARENT_FORUM, ForumPostId);
        query.findInBackground(new FindCallback<AnswerPost>() {
            @Override
            public void done(List<AnswerPost> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting answers", e);
                    Toast.makeText(PostActivity.this, "Issue with getting answers!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "All answers");
                    answers.addAll(items);
                    PostAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
