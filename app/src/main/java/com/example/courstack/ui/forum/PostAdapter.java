package com.example.courstack.ui.forum;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courstack.R;
import com.example.courstack.models.AnswerPost;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostAdapter{
    private final Context context;
    private final ArrayList<AnswerPost> answers;

    public PostAdapter(Context context, ArrayList<AnswerPost> answers) {
        this.context = context;
        this.answers = answers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvCourseTitle;
        private final TextView tvQuestionTitle;
        private final TextView tvLastUpdate;
        private final RecyclerView rvAnswerPost;
        private final ImageView ivPostProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseTitle = itemView.findViewById(R.id.tvQuestionTitle);
            tvQuestionTitle = itemView.findViewById(R.id.tvDescription);
            tvLastUpdate = itemView.findViewById(R.id.updateText);
            rvAnswerPost = itemView.findViewById(R.id.rvAnswerPost);
            ivPostProfile = itemView.findViewById(R.id.ivProfileRvAnswer);
        }
    }
}
