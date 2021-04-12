package com.example.courstack;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentDialogFragment extends DialogFragment {
    private static final int MAX_LENGTH = 3000;
    private EditText etResponse;
    private EditText etTitle;
    private Button btSend;
    private int titleDisabled;
    private AnswerPost answerPost;
    private TextInputLayout textLayout;

    public CommentDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CommentDialogFragment newInstance(AnswerPost answerPost, int titleDisabled) {
        CommentDialogFragment frag = new CommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("answerPost", answerPost);
        bundle.putInt("titleDisabled", titleDisabled);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        answerPost = getArguments().getParcelable("answerPost");
        titleDisabled = getArguments().getInt("titleDisabled");
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etResponse = view.findViewById(R.id.etResponse2);
        etTitle = view.findViewById(R.id.etTitle2);
        btSend = view.findViewById(R.id.btSend2);
        textLayout = view.findViewById(R.id.textLayout1);
        if(titleDisabled == 1){
            etTitle.setVisibility(View.INVISIBLE);
            textLayout.setVisibility(View.INVISIBLE);
        }
        btSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String dialogContent = etResponse.getText().toString();

                if (dialogContent.isEmpty()) {
                    Toast.makeText(getActivity(), "Sorry, your content cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (dialogContent.length() > MAX_LENGTH) {
                    Toast.makeText(getActivity(), "Sorry, your content is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                saveAnswerPost(dialogContent, answerPost, ParseUser.getCurrentUser());
            }
        });

    }

    public void saveAnswerPost(String dialogContent, AnswerPost answerPost,ParseUser user) {
        Answer answer = new Answer();
        answer.setText(dialogContent);
        answer.setParent(answerPost);
        answer.setStudent(user);
        answer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Dialog", "ERROR when saving answer");
                    Toast.makeText(getActivity(), "Post saving failed", Toast.LENGTH_SHORT).show();
                }
                Log.i("Dialog", "answer saving success");
                Toast.makeText(getActivity(), "Post saving successes", Toast.LENGTH_SHORT).show();

                //dismiss and refresh
                Activity activity = getActivity();
                if(activity instanceof ResponseDialogFragment.MyDialogCloseListener)
                    ((ResponseDialogFragment.MyDialogCloseListener)activity).handleDialogClose();
                dismiss();
            }
        });
    }
}
