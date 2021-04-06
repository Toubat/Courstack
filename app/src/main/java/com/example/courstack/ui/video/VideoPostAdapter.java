package com.example.courstack.ui.video;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.models.VideoPost;
import com.parse.ParseFile;

import java.util.List;

public class VideoPostAdapter extends RecyclerView.Adapter<VideoPostAdapter.VideoPostViewHolder> {

    Context context;
    List<VideoPost> videoPosts;

    public VideoPostAdapter(Context context, List<VideoPost> videoPosts) {
        this.context = context;
        this.videoPosts = videoPosts;
    }

    @NonNull
    @Override
    public VideoPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item_post, parent, false);
        return new VideoPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPostViewHolder holder, int position) {
        VideoPost videoPost = videoPosts.get(position);
        holder.bind(videoPost);
    }

    @Override
    public int getItemCount() {
        return videoPosts.size();
    }

    class VideoPostViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfile;
        private TextView tvUsername;
        private TextView tvTitle;
        private ImageView ivFrontImage;
        private TextView tvNumComments;

        public VideoPostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfileVideoPost);
            tvUsername = itemView.findViewById(R.id.tvUserNameVideoPost);
            tvTitle = itemView.findViewById(R.id.tvVideoTitle);
            ivFrontImage = itemView.findViewById(R.id.ivFrontImage);
            tvNumComments = itemView.findViewById(R.id.tvNumComments);
        }

        public void bind(VideoPost videoPost) {
            tvUsername.setText(videoPost.getStudent().getUsername());
            tvTitle.setText(videoPost.getTitle());
            // tvNumComments.setText("1324");
            ParseFile image = videoPost.getFrontImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivProfile);
            }
        }
    }
}
