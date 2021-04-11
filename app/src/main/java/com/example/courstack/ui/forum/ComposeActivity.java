package com.example.courstack.ui.forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courstack.R;
import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.ForumPost;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComposeActivity extends AppCompatActivity {
    EditText etCompose;
    EditText etTitle;
    EditText etCategory;
    public static final String TAG = "ComposeActivity";
    public static final int MAX_LENGTH = 6000;
    Button btnCompose;
    EditText etCourse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Intent intent = getIntent();
        String course = intent.getStringExtra("course");

        etTitle = findViewById(R.id.etTitle);
        etCompose = findViewById(R.id.etCompose);
        btnCompose = findViewById(R.id.btnCompose);
        etCategory = findViewById(R.id.etCategory);
        etCourse = findViewById(R.id.etCourse);

        etCourse.setText(course);

        btnCompose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String composeContent = etCompose.getText().toString();
                String composeTitle = etTitle.getText().toString();
                String composeCategory = etCategory.getText().toString();
                String composeCourse = etCourse.getText().toString();

                if (composeContent.isEmpty() || composeTitle.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your content cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (composeTitle.length() > 100) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your title is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                if (composeContent.length() > MAX_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your content is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveForumPost(composeTitle, composeContent, composeCategory, composeCourse, currentUser);
                finish();
            }
        });
    }

    public void saveForumPost(String title, String content, String category, String course,ParseUser currentUser){
        ForumPost forumPost = new ForumPost();
        forumPost.setTitle(title);
        forumPost.setStudent(currentUser);
        forumPost.setDescription(content);
        forumPost.setCategory(category);
        forumPost.setCourse(course);

        forumPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "ERROR when saving");
                    Toast.makeText(ComposeActivity.this, "Error when saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post saving success");
                etCategory.setText("");
                etCompose.setText("");
                etTitle.setText("");
            }
        });
    }
}
