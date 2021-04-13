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
    File photoFile;
    File videoFile;
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
                onLaunchVideo(v);
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
                    onLaunchVideo(v);
                }
            }
        });
        ivCameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                String title = etTitle.getText().toString();
                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(UploadVideoActivity.this, "Description or title cannot be empty!", Toast.LENGTH_LONG).show();
                } else if (videoFile == null || photoFile == null) {
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
        post.setFrontImage(new ParseFile(photoFile));
        Log.i(TAG, "Video");
        post.setVideoFile(new ParseFile(videoFile));
        post.setCourse(course);
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
        videoFile = null;
        photoFile = null;
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

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(UploadVideoActivity.this, "com.codepath.fileprovider.courstack", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

        return file;
    }

    public void onLaunchVideo(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Create a File reference for future access
        videoFile = getVideoFileUri(videoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(UploadVideoActivity.this, "com.codepath.fileprovider.courstack", videoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getVideoFileUri(String videoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + videoFileName);

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(UploadVideoActivity.this, "Front Page Uploaded", Toast.LENGTH_LONG).show();
                /*
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ivCameraIcon.setImageBitmap(imageBitmap);
                 */
                // by this point we have the camera photo on disk
                Log.i(TAG, photoFile.getAbsolutePath());
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivCameraIcon.setImageBitmap(takenImage);

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
                /*
                videoUri = data.getData();
                vvVideoUpload.setVideoURI(videoUri);
                 */
                Log.i(TAG, videoFile.getAbsolutePath());
                vvVideoUpload.setVideoURI(Uri.fromFile(videoFile));
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
