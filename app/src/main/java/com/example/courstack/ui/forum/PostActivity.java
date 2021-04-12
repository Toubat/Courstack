package com.example.courstack.ui.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.ResponseDialogFragment;
import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.ForumPost;
import com.example.courstack.ui.AnswerPostAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements ResponseDialogFragment.MyDialogCloseListener {
    public static final String TAG = "postActivity";
    List<AnswerPost> answers;
    AnswerPostAdapter adapter;
    ForumPost mainForumPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_rv_answers);
        RecyclerView rvAnswerPosts = findViewById(R.id.rvAnswerPost);

        // Answer bar
        Toolbar answerBar = findViewById(R.id.answer_bar);
        this.setSupportActionBar(answerBar);

        // set the main question of the forumPost
        TextView tvQuestionTitle = findViewById(R.id.tvQuestionTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvLastUpdate = findViewById(R.id.tvLastUpdate);
        TextView tvName = findViewById(R.id.tvName);
        ImageView ivProfileRvAnswer = findViewById(R.id.ivProfileRvAnswer);

        Intent intent = getIntent();
        String ForumPostId = intent.getStringExtra("postId");

        //initialize the main question
        tvDescription.setText(intent.getStringExtra("description"));
        tvQuestionTitle.setText(intent.getStringExtra("questionTitle"));
        tvLastUpdate.setText(intent.getStringExtra("update"));
        tvName.setText(intent.getStringExtra("name"));

        ParseFile profile = intent.getParcelableExtra("image");
        if ( profile!= null) {
            Glide.with(PostActivity.this).load(profile.getUrl()).into(ivProfileRvAnswer);
        }

        // 0.data source
        answers = new ArrayList<>();
        queryForumPostById(ForumPostId); // this method call queryAnswerPosts implicitly

        // 1.Create adapter
        adapter = new AnswerPostAdapter(this, answers);

        // 2.Set the adapter on the recycler view
        rvAnswerPosts.setAdapter(adapter);

        // 3.Set the layout manager on the rv
        rvAnswerPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forum_dialog_toolbar, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reply) {
            showReplyDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showReplyDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ResponseDialogFragment frag = ResponseDialogFragment.newInstance(mainForumPost);
        frag.show(fm, "fragment_dialog");
    }


    //execute when dialog dismisses
    @Override
    public void handleDialogClose() {
        populateAnswerPosts();
    }

    public void queryAnswerPosts(ForumPost forumPost) {
        // Specify all forumPosts to query
        ParseQuery<AnswerPost> query = ParseQuery.getQuery(AnswerPost.class);
        query.include(AnswerPost.KEY_STUDENT);
        query.include(AnswerPost.KEY_ANSWER);
        query.include(AnswerPost.KEY_PARENT_FORUM);
        query.include("objectId");
        query.include("updatedAt");
        query.whereEqualTo(AnswerPost.KEY_PARENT_FORUM, forumPost);
        query.findInBackground(new FindCallback<AnswerPost>() {
            @Override
            public void done(List<AnswerPost> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting answers", e);
                    Toast.makeText(PostActivity.this, "Issue with getting answers!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "All answers");
                    Log.i(TAG, String.format("%d", items.size()));
                    answers.addAll(items);
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }

    public void queryForumPostById(String id) {
        // Specify which class to query
        ParseQuery<ForumPost> query = ParseQuery.getQuery(ForumPost.class);
        query.include(ForumPost.KEY_STUDENT);
        query.include(ForumPost.KEY_TITLE);
        query.include(ForumPost.KEY_DESCRIPTION);
        query.include(ForumPost.KEY_CATEGORY);
        query.include("updatedAt");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ForumPost>() {
            @Override
            public void done(List<ForumPost> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the ForumPost", e);
                    Toast.makeText(PostActivity.this, "Issue with getting the ForumPost", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "Fetch ForumPost success");
                    mainForumPost = items.get(0);

                    // initialize all following answers
                    queryAnswerPosts(mainForumPost);
                }
            }
        });
    }

    public void populateAnswerPosts() {
        answers.clear();
        queryAnswerPosts(mainForumPost);
        adapter.notifyDataSetChanged();
        Log.i(TAG, "HERE");
        for(AnswerPost i: answers){
            Log.i(TAG, String.format("id: %d", i.getObjectId()));
        }
    }

}
