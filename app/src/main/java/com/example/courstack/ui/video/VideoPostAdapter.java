package com.example.courstack.ui.video;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
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
import com.example.courstack.models.VideoPost;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
        private TextView tvLastUpdate;

        public VideoPostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfileVideoPost);
            tvUsername = itemView.findViewById(R.id.tvUserNameVideoPost);
            tvTitle = itemView.findViewById(R.id.tvVideoTitle);
            ivFrontImage = itemView.findViewById(R.id.ivFrontImage);
            tvNumComments = itemView.findViewById(R.id.tvNumComments);
            tvLastUpdate = itemView.findViewById(R.id.tvLastUpdateVideo);
        }

        public void bind(VideoPost videoPost) {
            tvUsername.setText(videoPost.getStudent().getUsername());
            tvTitle.setText(videoPost.getTitle());
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String strDate = dateFormat.format(videoPost.getUpdatedAt());
            tvLastUpdate.setText(strDate);
            // tvNumComments.setText("1324");
            int radius = 25;
            int margin = 10;
            Glide.with(context)
                 .load(videoPost.getFrontImage().getUrl())
                 .transform(new RoundedCornersTransformation(radius, margin))
                 .into(ivFrontImage);
            Glide.with(context)
                 .load(videoPost.getStudent().getParseFile("profile_image").getUrl())
                 .transform(new RoundedCornersTransformation(radius, 5))
                 .into(ivProfile);
            ivFrontImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Nevigate to a new activity on tap
                    Intent i = new Intent(context, VideoPostActivity.class);
                    i.putExtra("username", videoPost.getStudent().getUsername());
                    i.putExtra("title", videoPost.getTitle());
                    i.putExtra("profileImage", videoPost.getStudent().getParseFile("profile_image").getUrl());
                    i.putExtra("videoUrl", videoPost.getVideoFile().getUrl());
                    i.putExtra("frontImage", videoPost.getFrontImage().getUrl());
                    i.putExtra("objectId", videoPost.getObjectId());
                    context.startActivity(i);
                }
            });
        }
    }
}
