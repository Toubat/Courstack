package com.example.courstack.ui.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.courstack.CommentDialogFragment;
import com.example.courstack.MainActivity;
import com.example.courstack.R;
import com.example.courstack.ResponseDialogFragment;
import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.VideoPost;
import com.example.courstack.ui.AnswerPostAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class VideoPostActivity extends AppCompatActivity implements ResponseDialogFragment.ResponseDialogListener, CommentDialogFragment.CommentDialogListener  {

    public static final String TAG = "VideoPostActivity";
    public static final String PLAYBACK_TIME = "play_time";

    private String videoUrl;
    private int currentPosition = 0;
    private boolean firstTime = true;

    ImageView ivProfile;
    ImageView ivPlayIcon;
    ImageView ivFrontImage;
    TextView tvUsername;
    TextView tvTitle;
    VideoView vvVideo;
    VideoPost videoPost;
    RecyclerView rvCommentPosts;
    List<AnswerPost> answerPosts;
    AnswerPostAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post);

        // Answer bar
        Toolbar video_bar = findViewById(R.id.video_bar);
        this.setSupportActionBar(video_bar);

        // get intent
        String username = getIntent().getStringExtra("username");
        String title = getIntent().getStringExtra("title");
        String profileImage = getIntent().getStringExtra("profileImage");
        String frontImage = getIntent().getStringExtra("frontImage");
        String videoName = getIntent().getStringExtra("videoUrl");
        String objectId = getIntent().getStringExtra("objectId");

        // set UI components
        ivProfile = findViewById(R.id.ivProfileVideoPost);
        ivPlayIcon = findViewById(R.id.ivPlayIcon);
        ivFrontImage = findViewById(R.id.ivFrontImage2);
        tvUsername = findViewById(R.id.tvUserNameVideoPost);
        tvTitle = findViewById(R.id.tvVideoTitle);
        vvVideo = findViewById(R.id.vvVideo);
        rvCommentPosts = findViewById(R.id.rvCommentPost);
        videoUrl = videoName;

        // populate data
        tvUsername.setText(username);
        tvTitle.setText(title);
        int radius = 25;
        int margin = 5;
        Glide.with(this).load(frontImage).into(ivFrontImage);
        Glide.with(this)
                .load(profileImage)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(ivProfile);
        ivPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstTime) {
                    initializePlayer();
                } else {
                    ivPlayIcon.setVisibility(ImageView.INVISIBLE);
                    vvVideo.start();
                }
            }
        });
        if (savedInstanceState != null) {
            Log.i(TAG, String.format("%d", currentPosition));
            currentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        } else {
            Log.i(TAG, "not saved");
        }

        // set media controller to enable full control of video
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(vvVideo);
        controller.setAnchorView(rvCommentPosts);
        vvVideo.setMediaController(controller);
        vvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setVisibility(View.VISIBLE);
            }
        });

        // set RecyclerView
        answerPosts = new ArrayList<>();
        adapter = new AnswerPostAdapter(this, answerPosts);
        rvCommentPosts.setAdapter(adapter);
        rvCommentPosts.setLayoutManager(new LinearLayoutManager(this));
        rvCommentPosts.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollX != oldScrollX || scrollY != oldScrollY) {
                    controller.setVisibility(View.INVISIBLE);
                }
            }
        });
        queryMainVideoPost(objectId);
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
            showReplyDialog(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showReplyDialog(int titleDisabled) {
        FragmentManager fm = getSupportFragmentManager();

        ResponseDialogFragment frag = ResponseDialogFragment.newInstance(videoPost, titleDisabled, "VideoPost");
        frag.show(fm, "fragment_dialog");
    }

    //execute when dialog dismisses
    @Override
    public void onFinishResponseDialog(AnswerPost answerPost) {
        answerPosts.add(0, answerPost);
        adapter.notifyItemInserted(0);
        // scroll to top of view
        rvCommentPosts.smoothScrollToPosition(0);
    }

    @Override
    public void onFinishCommentDialog(AnswerPost answerPost) {
        int position = answerPosts.indexOf(answerPost);
        if (position == -1) {
            Log.e(TAG, "Index out of bound error");
            throw new IllegalArgumentException("Index not exist");
        }
        adapter.notifyItemChanged(position);
    }

    private void queryMainVideoPost(String objectId) {
        ParseQuery<VideoPost> query = ParseQuery.getQuery(VideoPost.class);
        query.whereEqualTo("objectId", objectId);
        query.findInBackground(new FindCallback<VideoPost>() {
            @Override
            public void done(List<VideoPost> objects, ParseException e) {
                if (e == null) {
                    videoPost = objects.get(0);
                    Log.i(TAG, "Id is: " + videoPost.getObjectId());
                    queryAnswerPosts(videoPost);
                } else {
                    Log.e(TAG, "Issue with getting video posts", e);
                    Toast.makeText(VideoPostActivity.this, "Issue with getting video posts!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryAnswerPosts(VideoPost videoPost) {
        Log.i(TAG, videoPost.getObjectId());
        // Specify which class to query
        ParseQuery<AnswerPost> query = ParseQuery.getQuery(AnswerPost.class);
        query.include(AnswerPost.KEY_ANSWER);
        query.include(AnswerPost.KEY_STUDENT);
        query.include(AnswerPost.KEY_PARENT_VIDEO);
        query.whereEqualTo(AnswerPost.KEY_PARENT_VIDEO, videoPost);
        query.findInBackground(new FindCallback<AnswerPost>() {
            public void done(List<AnswerPost> items, ParseException e) {
                if (e == null) {
                    answerPosts.addAll(items);
                    Log.i(TAG, "All items: " + answerPosts);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Issue with getting answer posts", e);
                    Toast.makeText(VideoPostActivity.this, "Issue with getting answer posts!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializePlayer() {
        Log.i(TAG, "initialize player");
        ivPlayIcon.setVisibility(ImageView.VISIBLE);
        ivFrontImage.setVisibility(ImageView.VISIBLE);
        Uri videoUri = getMedia(videoUrl);
        vvVideo.setVideoURI(videoUri);
        vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ivPlayIcon.setVisibility(ImageView.INVISIBLE);
                ivFrontImage.setVisibility(ImageView.INVISIBLE);
                if (currentPosition > 0) {
                    // move the playback position to the current position
                    vvVideo.seekTo(currentPosition);
                } else {
                    // Skipping to 1 shows the first frame of the video rather than a black screen
                    vvVideo.seekTo(1);
                }
                vvVideo.start();
            }
        });
        vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoPostActivity.this, "Video completed", Toast.LENGTH_SHORT).show();
                firstTime = false;
                vvVideo.seekTo(1);
                ivPlayIcon.setVisibility(ImageView.VISIBLE);
            }
        });
    }

    private void releasePlayer() {
        vvVideo.stopPlayback();
    }

    private Uri getMedia(String mediaName) {
        return Uri.parse(mediaName);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            vvVideo.pause();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, String.format("%d", vvVideo.getCurrentPosition()));
        outState.putInt(PLAYBACK_TIME, vvVideo.getCurrentPosition());
    }
}