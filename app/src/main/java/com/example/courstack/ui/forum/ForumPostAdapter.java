package com.example.courstack.ui.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.models.ForumPost;
import com.parse.ParseFile;

import java.util.List;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ForumPostViewHolder> {

    private Context context;
    private List<ForumPost> forumPosts;

    public ForumPostAdapter(Context context, List<ForumPost> forumPosts) {
        this.context = context;
        this.forumPosts = forumPosts;
    }

    @NonNull
    @Override
    public ForumPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forum_item_post, parent, false);
        return new ForumPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumPostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ForumPostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivProfile;
        private final TextView tvQuestionTitle;
        private final TextView tvQuestion;
        private final TextView tvLateUpdate;

        public ForumPostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfileForumPost);
            tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
            tvQuestion = itemView.findViewById(R.id.tvDescription);
            tvLateUpdate = itemView.findViewById(R.id.tvLastUpdate);
        }

        public void bind(ForumPost forumPost) {
            tvQuestionTitle.setText(forumPost.getTitle());
            tvQuestion.setText(forumPost.getDescription());
            tvLateUpdate.setText((CharSequence) forumPost.getUpdatedAt());
            ParseFile image = forumPost.getStudent().getParseFile("profile_image");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivProfile);
            }
        }
    }
}
