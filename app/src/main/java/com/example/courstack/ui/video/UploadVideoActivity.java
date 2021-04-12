package com.example.courstack.ui.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.courstack.R;

import java.io.File;

public class UploadVideoActivity extends AppCompatActivity {

    public static final String TAG = "UploadVideoActivity";
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 1;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private String videoFileName = "video.mp4";
    private String photoFileName = "photo.jpg";
    private File videoFile;
    private File photoFile;
    Uri photoUri;
    Uri videoUri;
    private boolean firstTime = true;
    private boolean uploaded = false;

    EditText etTitle;
    EditText etDescription;
    ImageView ivVideoUploader;
    ImageView ivVideoIcon;
    ImageView ivCameraIcon;
    VideoView vvVideoUpload;
    Button btnUpload;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        etTitle = findViewById(R.id.etVideoTitle);
        etDescription = findViewById(R.id.etDescription);
        ivVideoUploader = findViewById(R.id.ivVideoUploader);
        ivVideoIcon = findViewById(R.id.ivVideoIcon);
        ivCameraIcon = findViewById(R.id.ivCameraIcon);
        vvVideoUpload = findViewById(R.id.vvVideoUpload);
        btnUpload = findViewById(R.id.btnUpload);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setVisibility(View.INVISIBLE);
        vvVideoUpload.setBackgroundColor(getResources().getColor(R.color.black));
        ivVideoUploader.setVisibility(View.VISIBLE);
        ivCameraIcon.setVisibility(View.INVISIBLE);

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(vvVideoUpload);
        vvVideoUpload.setMediaController(controller);

        ivVideoUploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchVideoRecording();
            }
        });
        ivVideoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploaded) {
                    ivVideoIcon.setVisibility(View.INVISIBLE);
                    vvVideoUpload.start();
                } else {
                    onLaunchVideoRecording();
                }
            }
        });
        ivCameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchPhotoTaking();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvVideoUpload.stopPlayback();
                vvVideoUpload.setVideoURI(null);
                vvVideoUpload.setBackgroundColor(getResources().getColor(R.color.black));
                uploaded = false;
                ivVideoIcon.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);
                ivCameraIcon.setVisibility(View.INVISIBLE);

                ivCameraIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_camera_alt_24));
                ivVideoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_videocam_24));
            }
        });
    }

    public void onLaunchPhotoTaking() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void onLaunchVideoRecording() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(UploadVideoActivity.this, "Front Page Uploaded", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivCameraIcon.setImageBitmap(imageBitmap);
                vvVideoUpload.seekTo(1);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (resultCode == RESULT_OK) {
                Toast.makeText(UploadVideoActivity.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                ivVideoUploader.setVisibility(View.INVISIBLE);
                videoUri = data.getData();
                vvVideoUpload.setVideoURI(videoUri);
                initializePlayer();
                vvVideoUpload.setBackgroundResource(0);
                ivCameraIcon.setVisibility(View.VISIBLE);
                ivVideoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
            } else {
                Toast.makeText(UploadVideoActivity.this, "Unable to upload video", Toast.LENGTH_SHORT).show();
            }
        }
        uploaded = true;
        btnCancel.setVisibility(View.VISIBLE);
    }

    public void initializePlayer() {
        vvVideoUpload.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "completion");
                Toast.makeText(UploadVideoActivity.this, "Video completed", Toast.LENGTH_SHORT).show();
                vvVideoUpload.seekTo(1);
                ivVideoIcon.setVisibility(ImageView.VISIBLE);
                firstTime = false;
            }
        });
        Log.i(TAG, "init");
        vvVideoUpload.seekTo(1);
    }

    private void releasePlayer() {
        vvVideoUpload.stopPlayback();
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
}