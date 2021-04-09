package com.example.courstack.ui.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.courstack.R;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.ui.AnswerPostAdapter;

import java.util.List;

public class VideoPostActivity extends AppCompatActivity {

    public static final String TAG = "VideoPostActivity";

    ImageView ivProfile;
    TextView tvUsername;
    TextView tvTitle;
    VideoView vvVideo;
    RecyclerView rvCommentPosts;
    List<AnswerPost> answerPosts;
    AnswerPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);

        ivProfile = findViewById(R.id.ivProfileVideoPost);
        tvUsername = findViewById(R.id.tvUserNameVideoPost);
        tvTitle = findViewById(R.id.tvVideoTitle);
        vvVideo = findViewById(R.id.vvVideo);

    }
}