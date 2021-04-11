package com.example.courstack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.courstack.models.Answer;
import com.example.courstack.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SelectCourseActivity extends AppCompatActivity {

    public static final String TAG = "SelectCourseActivity";

    List<Course> courses;
    RecyclerView rvCourses;
    CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);

        courses = new ArrayList<>();
        rvCourses = findViewById(R.id.rvCourses);
        adapter = new CourseAdapter(this, courses);
        rvCourses.setAdapter(adapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        queryCourse();
    }

    private void queryCourse() {
        ParseUser user = ParseUser.getCurrentUser();
        // Specify which class to query
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.include(Course.KEY_COURSE_TITLE);
        query.include(Course.KEY_COURSE_NAME);
        query.include(Course.KEY_BACKGROUND);
        query.include(Course.KEY_INSTRUCTOR);
        // query.whereEqualTo(user.get(...), answerPost);
        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting courses", e);
                    Toast.makeText(SelectCourseActivity.this, "Issue with getting courses!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "All answers");
                    courses.addAll(items);
                    Log.i(TAG, String.valueOf(courses));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}