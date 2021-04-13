package com.example.courstack.ui.video;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.courstack.R;
import com.example.courstack.models.Course;
import com.example.courstack.models.VideoPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    public static final String TAG = "VideoFragment";

    public final int REQUEST_CODE = 20;
    RecyclerView rvVideoPost;
    VideoPostAdapter adapter;
    List<VideoPost> videoPosts;
    SwipeRefreshLayout swipeContainer;
    FloatingActionButton floatingActionButton;
    private Course course;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar videoToolbar = getActivity().findViewById(R.id.video_bar);
        // videoToolbar.setNavigationIcon(R.drawable.ic_baseline_video_library_24);
        ((AppCompatActivity) getActivity()).setSupportActionBar(videoToolbar);
        rvVideoPost = view.findViewById(R.id.rvVideoPosts);
        floatingActionButton =  view.findViewById(R.id.floatingButton);
        // 0. Create the data source
        videoPosts = new ArrayList<>();
        // 1. Create the adapter
        adapter = new VideoPostAdapter(getContext(), videoPosts);
        // 2. Set the adapter on the recycler view
        rvVideoPost.setAdapter(adapter);
        rvVideoPost.setLayoutManager(new LinearLayoutManager(getContext()));
        //configure swipeContainer
        swipeContainer = getActivity().findViewById(R.id.swipe_container_video);
        // Scheme colors for animation
        swipeContainer.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                populateVideoPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), UploadVideoActivity.class);
                i.putExtra("course", course);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        queryVideoPosts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.i(TAG, "Success to return to the rv");
            populateVideoPosts();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateVideoPosts() {
        videoPosts.clear();
        queryVideoPosts();
    }

    private void queryVideoPosts() {
        ParseQuery<VideoPost> query = ParseQuery.getQuery(VideoPost.class);
        query.include(VideoPost.KEY_FRONT_IMAGE);
        query.include(VideoPost.KEY_STUDENT);
        query.include(VideoPost.KEY_TITLE);
        query.whereEqualTo(VideoPost.KEY_COURSE, course);
        query.include("updatedAt");
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<VideoPost>() {
            @Override
            public void done(List<VideoPost> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    Toast.makeText(getContext(), "Issue with getting posts!", Toast.LENGTH_SHORT).show();
                } else {
                    videoPosts.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.video_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            Toast.makeText(getContext(), "Menu item was clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}