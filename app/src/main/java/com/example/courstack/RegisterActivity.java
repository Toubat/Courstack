package com.example.courstack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private EditText etUsername;
    private EditText etPassword;
    private TextView btnReturn;
    private Button btnSignup;
    private Button btnAvatar;
    private ImageView ivAvatar;
    private TextView etDescription;
    private TextView etMajor;
    private ParseFile parseImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btCreate);
        btnReturn = findViewById(R.id.btReturn);
        btnAvatar = findViewById(R.id.btAvatar);
        ivAvatar = findViewById(R.id.ivProfileAvatar);
        etDescription = findViewById(R.id.etDescription);
        etMajor = findViewById(R.id.etMajor);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick return button");
                returnLogin();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String description = etDescription.getText().toString();
                String major = etMajor.getText().toString();
                signupUser(username, password, description, major, parseImage);
            }
        });

        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(RegisterActivity.this);
            }
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ivAvatar.setImageBitmap(selectedImage);
                        bitmapConvertParseImage(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                ivAvatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                bitmapConvertParseImage(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void signupUser(String username, String password, String description, String major, ParseFile avatar) {
        Log.i(TAG, "Attempting to signup user " + username);
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        if(parseImage == null){
            convertDrawableToParsefile();
        }
        user.put("description", description);
        user.put("major", major);
        parseImage.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // If successful add file to user and signUpInBackground
                if (e != null) {
                    Log.e(TAG, "Issue with save", e);
                    Toast.makeText(RegisterActivity.this, "Issue with save!", Toast.LENGTH_SHORT).show();
                }
                if(e == null)
                    user.put("profile_image",parseImage);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issue with signup", e);
                                Toast.makeText(RegisterActivity.this, "Issue with Signup!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                                etUsername.setText("");
                                etPassword.setText("");
                                returnLogin();
                            }
                        }
                    });
            }
        });

    }

    private void convertDrawableToParsefile() {
        Drawable myDrawable = getResources().getDrawable(R.drawable.ic_default_profile_image);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        ByteArrayOutputStream myLogoStream = new ByteArrayOutputStream();
        myLogo.compress(Bitmap.CompressFormat.PNG, 100, myLogoStream);
        byte[] myLogoByteArray = myLogoStream.toByteArray();
        myLogo.recycle();
        parseImage = new ParseFile("default.png", myLogoByteArray);
    }

    public void bitmapConvertParseImage(Bitmap map){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        map.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        // Create the ParseFile
        parseImage = new ParseFile("picture_1.jpeg", image);
    }

    private void returnLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        // remove activity from stack
        finish();
    }
}