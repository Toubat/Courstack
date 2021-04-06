package com.example.courstack.ui.video;

import android.content.Context;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courstack.models.VideoPost;

import java.util.List;

public class VideoPostAdapter extends RecyclerView.Adapter<VideoPostAdapter.VideoPostViewHolder> {

    Context context;
    List<VideoPost> videoPosts;

    @NonNull
    @Override
    public VideoPostAdapter.VideoPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPostAdapter.VideoPostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class VideoPostViewHolder extends RecyclerView.ViewHolder {

        public VideoPostViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
