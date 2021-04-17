package com.example.courstack.ui.classmate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.courstack.R;
import com.example.courstack.models.Course;
import com.example.courstack.models.ForumPost;
import com.example.courstack.models.Student;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ClassmateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ClassmateFragment";

    // TODO: Rename and change types of parameters
    private RecyclerView rvStudent;
    private StudentAdapter adapter;
    private List<ParseUser> allStudents;
    private Course course;

    public ClassmateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    /*public static ClassmateFragment newInstance(String param1, String param2) {
        ClassmateFragment fragment = new ClassmateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classmate, container, false);
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvStudent = view.findViewById(R.id.rvStudent);


        //Steps to create a recycler view:
        //1.create a layout for one row in the list
        //DONE in item_post.xml!
        //2.create the adaptor
        allStudents = new ArrayList<>();
        adapter = new StudentAdapter(getContext(), allStudents);
        //3.create the data source
        //DONE in Student class
        //4.set the adaptor on the recycler view
        rvStudent.setAdapter(adapter);
        //5.set the layout manager on the recycler view
        rvStudent.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void queryPosts() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("username");
        query.include("major");
        query.include("description");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> students, ParseException e) {
                if (e == null) {
                    for (ParseUser student : students) {
                        Log.i(TAG, "Student: " + student.get("description") + "; User: " + student.getUsername());
                    }
                } else {
                    // Something went wrong.
                    Log.e(TAG, "Issue with getting students", e);
                }
                allStudents.addAll(students);
                adapter.notifyDataSetChanged();
            }
        });
    }
}