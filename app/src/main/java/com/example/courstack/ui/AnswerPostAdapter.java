package com.example.courstack.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.models.Answer;
import com.example.courstack.models.AnswerPost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AnswerPostAdapter extends RecyclerView.Adapter<AnswerPostAdapter.AnswerPostViewHolder> {

    public static final String TAG = "AnswerPostAdapter";
    Context context;
    List<AnswerPost> answerPosts;

    public AnswerPostAdapter(Context context, List<AnswerPost> answerPosts) {
        this.context = context;
        this.answerPosts = answerPosts;
    }

    @NonNull
    @Override
    public AnswerPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_answer_container, parent, false);
        return new AnswerPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerPostViewHolder holder, int position) {
        AnswerPost answerPost = answerPosts.get(position);
        holder.bind(answerPost);
    }

    @Override
    public int getItemCount() {
        return answerPosts.size();
    }

    class AnswerPostViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvUsername;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvLastUpdate;
        RecyclerView rvAnswers;
        AnswerAdapter adapter;
        List<Answer> answers;

        public AnswerPostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile2);
            tvUsername = itemView.findViewById(R.id.tvUsernameForumPost2);
            tvTitle = itemView.findViewById(R.id.tvAnswerTitle2);
            tvDescription = itemView.findViewById(R.id.tvDescription2);
            tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate2);
            rvAnswers = itemView.findViewById(R.id.rvAnswers);
            answers = new ArrayList<>();
            adapter = new AnswerAdapter(context, answers, AnswerAdapter.BRIEF);
            // 2. Set the adapter on the recycler view
            rvAnswers.setAdapter(adapter);
            rvAnswers.setLayoutManager(new LinearLayoutManager(context));
        }

        public void bind(AnswerPost answerPost) {
            tvUsername.setText(answerPost.getStudent().getUsername());
            tvTitle.setText(answerPost.getAnswer().getTitle());
            tvDescription.setText(answerPost.getAnswer().getText());

            //date
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String strDate = dateFormat.format(answerPost.getAnswer().getUpdatedAt());
            tvLastUpdate.setText(strDate);

            ParseFile profile = answerPost.getStudent().getParseFile("profile_image");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).into(ivProfile);
            }
            queryAnswers(answerPost);
            adapter.notifyDataSetChanged();
        }

        public void queryAnswers(AnswerPost answerPost) {
            // Specify which class to query
            ParseQuery<Answer> query = ParseQuery.getQuery(Answer.class);
            query.include(Answer.KEY_STUDENT);
            query.include(Answer.KEY_ANSWER_TEXT);
            query.include(Answer.KEY_PARENT);
            query.whereEqualTo(Answer.KEY_PARENT, answerPost);
            query.findInBackground(new FindCallback<Answer>() {
                @Override
                public void done(List<Answer> items, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting answers", e);
                        Toast.makeText(context, "Issue with getting answers!", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG, "All answers");
                        answers.addAll(items);
                    }
                }
            });
        }

    }

}
