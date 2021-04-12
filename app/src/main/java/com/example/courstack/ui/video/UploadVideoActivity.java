package com.example.courstack.ui.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    private static final int VIDEO_CAPTURE = 101;
    Uri videoUri;

    EditText etTitle;
    EditText etDescription;
    ImageView ivVideoUploader;
    ImageView ivVideoIcon;
    ImageView ivCameraIcon;
    VideoView vvVideoUpload;
    Button btnUpload;

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

        ivVideoUploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecordingVideo();
            }
        });
        ivCameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void startRecordingVideo() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
            videoUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(intent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                ivVideoUploader.setVisibility(View.INVISIBLE);
                ivVideoIcon.setVisibility(View.INVISIBLE);
                playbackRecordedVideo();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void playbackRecordedVideo() {
        vvVideoUpload.setVideoURI(videoUri);
        vvVideoUpload.setMediaController(new MediaController(this));
        vvVideoUpload.requestFocus();
        vvVideoUpload.start();
    }
}