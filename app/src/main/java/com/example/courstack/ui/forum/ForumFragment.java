package com.example.courstack.ui.forum;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.courstack.R;
import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.ForumPost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    public static final String TAG = "ForumFragment";
    List<ForumPost> forumPosts;
    ForumPostAdapter adapter;
    RecyclerView rvForumPosts;
    private String course;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
<<<<<<< HEAD
    private String course = "CMPSC 465";
=======
>>>>>>> 7e907eee5a73ff3b85438f3c9be8477aa638d69e

    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(String param1, String param2) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
<<<<<<< HEAD
=======
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
>>>>>>> 7e907eee5a73ff3b85438f3c9be8477aa638d69e
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar forumToolbar = getActivity().findViewById(R.id.forum_bar);
        // videoToolbar.setNavigationIcon(R.drawable.ic_baseline_video_library_24);
        ((AppCompatActivity) getActivity()).setSupportActionBar(forumToolbar);

        rvForumPosts = view.findViewById(R.id.rvForumPosts);
        //0. create data source
        forumPosts = new ArrayList<>();
        queryForumPost(course);
        //1. create adapter
        adapter = new ForumPostAdapter(getContext(), forumPosts);
        //2. set adapter
        rvForumPosts.setAdapter(adapter);
        rvForumPosts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forum_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_compose) {
            Toast.makeText(getContext(), "Menu item was clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void queryForumPost(String course) {
        // Specify which class to query
        ParseQuery<ForumPost> query = ParseQuery.getQuery(ForumPost.class);
        query.include(ForumPost.KEY_STUDENT);
        query.include(ForumPost.KEY_TITLE);
        query.include(ForumPost.KEY_DESCRIPTION);
        query.include(ForumPost.KEY_CATEGORY);
        query.include("updatedAt");
        query.whereEqualTo(ForumPost.KEY_COURSE, course);
        query.findInBackground(new FindCallback<ForumPost>() {
            @Override
            public void done(List<ForumPost> items, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting answers", e);
                    Toast.makeText(getContext(), "Issue with getting answers!", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "All answers");
                    forumPosts.addAll(items);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}