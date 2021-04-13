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
import android.os.Build;
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
import com.example.courstack.models.Course;
import com.example.courstack.models.VideoPost;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UploadVideoActivity extends AppCompatActivity {

    public static final String TAG = "UploadVideoActivity";
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 1;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private String videoFileName = "video.mp4";
    private String photoFileName = "photo.jpg";
    Uri videoUri;
    Bitmap imageBitmap;
    private boolean uploaded = false;

    EditText etTitle;
    EditText etDescription;
    ImageView ivVideoUploader;
    ImageView ivVideoIcon;
    ImageView ivCameraIcon;
    VideoView vvVideoUpload;
    Button btnUpload;
    Button btnCancel;
    MediaController controller;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        course = getIntent().getParcelableExtra("course");

        etTitle = findViewById(R.id.etVideoTitle);
        etDescription = findViewById(R.id.etDescription);
        ivVideoUploader = findViewById(R.id.ivVideoUploader);
        ivVideoIcon = findViewById(R.id.ivVideoIcon);
        ivCameraIcon = findViewById(R.id.ivCameraIcon);
        vvVideoUpload = findViewById(R.id.vvVideoUpload);
        btnUpload = findViewById(R.id.btnUpload);
        btnCancel = findViewById(R.id.btnCancel);

        // initial setting
        btnCancel.setVisibility(View.INVISIBLE);
        vvVideoUpload.setBackgroundColor(getResources().getColor(R.color.teal_200));
        ivVideoUploader.setVisibility(View.VISIBLE);
        ivCameraIcon.setVisibility(View.INVISIBLE);

        controller = new MediaController(this);
        controller.setMediaPlayer(vvVideoUpload);
        vvVideoUpload.setMediaController(controller);
        controller.setAnchorView(vvVideoUpload);

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
                    controller.setVisibility(View.VISIBLE);
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
                String description = etDescription.getText().toString();
                String title = etTitle.getText().toString();
                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(UploadVideoActivity.this, "Description or title cannot be empty!", Toast.LENGTH_LONG).show();
                } else if (videoUri == null || imageBitmap == null) {
                    Toast.makeText(UploadVideoActivity.this, "Video or image source cannot be empty!", Toast.LENGTH_LONG).show();
                } else {
                    ParseUser user = ParseUser.getCurrentUser();
                    saveVideoPost(user, description, title);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMediaSource();
            }
        });
    }

    private void saveVideoPost(ParseUser user, String description, String title) {
        VideoPost post = new VideoPost();
        post.setStudent(user);
        post.setTitle(title);
        Log.i(TAG, "Front Image");
        post.setFrontImage(fromImageBitmap());
        Log.i(TAG, "Video");
        post.setVideoFile(fromVideoUri());
        // post.setCourse(course);
        Log.i(TAG, "Attempting to upload video post");
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while uploading!", e);
                    Toast.makeText(UploadVideoActivity.this, "Error while uploading!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "Upload successful!");
                    Toast.makeText(UploadVideoActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                    etDescription.setText("");
                    etTitle.setText("");
                    resetMediaSource();
                }
            }
        });
    }

    private void resetMediaSource() {
        uploaded = false;
        imageBitmap = null;
        videoUri = null;
        // reset VideoView
        vvVideoUpload.stopPlayback();
        vvVideoUpload.setVideoURI(null);
        vvVideoUpload.setBackgroundColor(getResources().getColor(R.color.teal_200));
        // reset icons
        ivVideoIcon.setVisibility(View.VISIBLE);
        ivVideoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_videocam_24));
        ivCameraIcon.setVisibility(View.INVISIBLE);
        ivCameraIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_camera_alt_24));
        // disable some UI
        btnCancel.setVisibility(View.INVISIBLE);
        controller.setVisibility(View.INVISIBLE);
    }

    public ParseFile fromImageBitmap(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();

        return new ParseFile("image_file.png", imageByte);
    }

    public ParseFile fromVideoUri() {
        File file = new File(videoUri.getPath());

        return new ParseFile(file);
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
                Toast.makeText(UploadVideoActivity.this, "Front Page Uploaded", Toast.LENGTH_LONG).show();
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ivCameraIcon.setImageBitmap(imageBitmap);

                uploaded = true;
                btnCancel.setVisibility(View.VISIBLE);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_LONG).show();
            }
            vvVideoUpload.seekTo(1);
        } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(UploadVideoActivity.this, "Video Uploaded", Toast.LENGTH_LONG).show();
                ivVideoUploader.setVisibility(View.INVISIBLE);
                videoUri = data.getData();
                vvVideoUpload.setVideoURI(videoUri);
                initializePlayer();
                vvVideoUpload.setBackgroundResource(0);
                ivCameraIcon.setVisibility(View.VISIBLE);
                ivVideoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));

                uploaded = true;
                btnCancel.setVisibility(View.VISIBLE);
                // fromVideoUri();
            } else {
                Toast.makeText(UploadVideoActivity.this, "Unable to upload video", Toast.LENGTH_LONG).show();
            }
        } else {
            throw new IllegalArgumentException("request code does not exist");
        }
    }

    public void initializePlayer() {
        vvVideoUpload.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "completion");
                Toast.makeText(UploadVideoActivity.this, "Video completed", Toast.LENGTH_SHORT).show();
                vvVideoUpload.seekTo(1);
                ivVideoIcon.setVisibility(ImageView.VISIBLE);
                controller.setVisibility(View.INVISIBLE);
            }
        });
        Log.i(TAG, "init");
        vvVideoUpload.seekTo(1);
        controller.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            vvVideoUpload.pause();
        }

    }
}
