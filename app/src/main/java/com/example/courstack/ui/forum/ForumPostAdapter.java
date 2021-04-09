package com.example.courstack.ui.forum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.models.ForumPost;
import com.parse.ParseFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.ForumPostViewHolder> {

    private Context context;
    private List<ForumPost> forumPosts;

    public ForumPostAdapter(Context context, List<ForumPost> forumPosts) {
        this.context = context;
        this.forumPosts = forumPosts;
    }

    //create a viewHolder: inflate
    @NonNull
    @Override
    public ForumPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forum_item_post, parent, false);
        return new ForumPostViewHolder(view);
    }

    //bind an item
    @Override
    public void onBindViewHolder(@NonNull ForumPostViewHolder holder, int position) {
        ForumPost forumPost = forumPosts.get(position);
        holder.bind(forumPost);
    }

    //get count
    @Override
    public int getItemCount() {
        return forumPosts.size();
    }

    class ForumPostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivProfile;
        private final TextView tvQuestionTitle;
        private final TextView tvQuestion;
        private final TextView tvLateUpdate;
        private final RelativeLayout relativeLayout;

        public ForumPostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfileForumPost);
            tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
            tvQuestion = itemView.findViewById(R.id.tvDescription);
            tvLateUpdate = itemView.findViewById(R.id.tvLastUpdate);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

        public void bind(ForumPost forumPost) {
            tvQuestionTitle.setText(forumPost.getTitle());
            tvQuestion.setText(forumPost.getDescription());

            //date
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String strDate = dateFormat.format(forumPost.getUpdatedAt());
            tvLateUpdate.setText(strDate);


            ParseFile image = forumPost.getStudent().getParseFile("profile_image");
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivProfile);
            }

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PostActivity.class);
                    i.putExtra("postId", forumPost.getObjectId());
                    context.startActivity(i);
                }
            });
        }
    }
}
