package com.example.courstack;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.example.courstack.models.ForumPost;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ResponseDialogFragment extends DialogFragment {
    private static final int MAX_LENGTH = 3000;
    private EditText etResponse;
    private EditText etTitle;
    private Button btSend;
    private ForumPost forumPost;
    private int titleDisabled;

    public ResponseDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ResponseDialogFragment newInstance(ForumPost forumPost, int titleDisabled) {
        ResponseDialogFragment frag = new ResponseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("forumPost", forumPost);
        bundle.putInt("titleDisabled", titleDisabled);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        forumPost = getArguments().getParcelable("forumPost");
        titleDisabled = getArguments().getInt("titleDisabled");
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//
//        Toolbar dialogToolbar = getActivity().findViewById(R.id.answer_bar);
//        // videoToolbar.setNavigationIcon(R.drawable.ic_baseline_video_library_24);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(dialogToolbar);


        etResponse = view.findViewById(R.id.etResponse2);
        etTitle = view.findViewById(R.id.etTitle2);
        btSend = view.findViewById(R.id.btSend2);
        if(titleDisabled == 1){
            etTitle.setVisibility(View.INVISIBLE);
        }
        btSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String dialogContent = etResponse.getText().toString();
                String dialogTitle = etTitle.getText().toString();

                if (dialogContent.isEmpty() || dialogTitle.isEmpty()) {
                    Toast.makeText(getActivity(), "Sorry, your content cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (dialogTitle.length() > 100) {
                    Toast.makeText(getActivity(), "Sorry, your title is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                if (dialogContent.length() > MAX_LENGTH) {
                    Toast.makeText(getActivity(), "Sorry, your content is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                saveAnswerPost(dialogTitle, dialogContent, forumPost, ParseUser.getCurrentUser());

            }
        });

    }

    // result return purpose interface
    public interface MyDialogCloseListener
    {
        public void handleDialogClose();
    }

//    //call handleDialogClose()
//    public void onDismiss(DialogInterface dialog)
//    {
//        dismiss();
//        Activity activity = getActivity();
//        if(activity instanceof MyDialogCloseListener)
//            ((MyDialogCloseListener)activity).handleDialogClose();
//    }

    public void saveAnswerPost(String dialogTitle, String dialogContent, ForumPost forumPost,ParseUser user) {
        Answer answer = new Answer();

        answer.setText(dialogContent);
        answer.setKeyTitle(dialogTitle);
        answer.setStudent(user);
        answer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Dialog", "ERROR when saving answer");
                    Toast.makeText(getActivity(), "Post saving failed", Toast.LENGTH_SHORT).show();
                }
                Log.i("Dialog", "answer saving success");
                subSaveAnswerPost(answer);
            }
        });
    }

    public void subSaveAnswerPost(Answer currentAnswer){
        AnswerPost answerPost = new AnswerPost();
        answerPost.setAnswerAndStudent(currentAnswer);
        answerPost.setParentForumPost(forumPost);

        answerPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Dialog", "ERROR when saving answerPost");
                    Toast.makeText(getActivity(), "Post saving failed", Toast.LENGTH_SHORT).show();
                }
                Log.i("Dialog", "answerpost saving success");
                Toast.makeText(getActivity(), "Post saving success", Toast.LENGTH_SHORT).show();
                Activity activity = getActivity();
                if(activity instanceof MyDialogCloseListener)
                    ((MyDialogCloseListener)activity).handleDialogClose();
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.95), (int) (size.y * 0.6));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
