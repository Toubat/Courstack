package com.example.courstack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.ui.classmate.ClassmateFragment;
import com.example.courstack.ui.forum.forumFragment;
import com.example.courstack.ui.profile.ProfileFragment;
import com.example.courstack.ui.video.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    AnswerPost answerPost;

    // Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.nav_videos:
                        fragment = new VideoFragment();
                        break;
                    case R.id.nav_notes:
                        fragment = new forumFragment();
                        break;
                    case R.id.nav_classmate:
                        fragment = new ClassmateFragment();
                        break;
                    case R.id.nav_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.nav_videos);
        queryAnswerPost();
    }

    protected void queryAnswerPost() {
        // Specify which class to query
        ParseQuery<AnswerPost> query = ParseQuery.getQuery(AnswerPost.class);
        query.include(AnswerPost.KEY_ANSWER);
        query.findInBackground(new FindCallback<AnswerPost>() {
            public void done(List<AnswerPost> items, ParseException e) {
                if (e == null) {
                    answerPost = items.get(0);
                    queryAnswers(answerPost);
                } else {
                    Log.e(TAG, "Issue with getting answer posts", e);
                    Toast.makeText(MainActivity.this, "Issue with getting answer posts!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void queryAnswers(AnswerPost answerPost) {
        // Specify which class to query
        ParseQuery<Answer> query = ParseQuery.getQuery(Answer.class);
        query.include(Answer.KEY_STUDENT);
        query.include(Answer.KEY_ANSWER_TEXT);
        query.include(Answer.KEY_PARENT);
        query.whereEqualTo(Answer.KEY_PARENT, answerPost);
        query.findInBackground(new FindCallback<Answer>() {
            @Override
            public void done(List<Answer> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting answers", e);
                    Toast.makeText(MainActivity.this, "Issue with getting answers!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "All answers");
                    for (Answer answer: objects) {
                        Log.i(TAG, answer.getText());
                    }
                }
            }
        });
    }
}