package com.example.courstack.ui.video;

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
import com.example.courstack.models.VideoPost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    public static final String TAG = "VideoFragment";
    RecyclerView rvVideoPost;
    VideoPostAdapter adapter;
    List<VideoPost> videoPosts;
    private String course;

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
        // 0. Create the data source
        videoPosts = new ArrayList<>();
        // 1. Create the adapter
        adapter = new VideoPostAdapter(getContext(), videoPosts);
        // 2. Set the adapter on the recycler view
        rvVideoPost.setAdapter(adapter);
        rvVideoPost.setLayoutManager(new LinearLayoutManager(getContext()));
        queryVideoPosts();
    }

    private void queryVideoPosts() {
        ParseQuery<VideoPost> query = ParseQuery.getQuery(VideoPost.class);
        query.include(VideoPost.KEY_FRONT_IMAGE);
        query.include(VideoPost.KEY_STUDENT);
        query.include(VideoPost.KEY_TITLE);
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

    public void setCourse(String course) {
        this.course = course;
    }
}