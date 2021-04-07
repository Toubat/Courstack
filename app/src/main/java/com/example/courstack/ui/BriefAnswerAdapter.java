package com.example.courstack.ui;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.models.Answer;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class BriefAnswerAdapter extends RecyclerView.Adapter<BriefAnswerAdapter.BriefAnswerViewHolder> {

    Context context;
    List<Answer> answers;

    public BriefAnswerAdapter(Context context, List<Answer> answers) {
        this.context = context;
        this.answers = answers;
    }

    @NonNull
    @Override
    public BriefAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brief_item_answer, parent, false);
        return new BriefAnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BriefAnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.bind(answer);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class BriefAnswerViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfile;
        TextView tvDescription;

        public BriefAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivSmallProfile);
            tvDescription = itemView.findViewById(R.id.tvBriefAnswer);
        }

        public void bind(Answer answer) {
            tvDescription.setText(answer.getText());
            ParseFile profile = answer.getStudent().getParseFile("profile_image");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).into(ivProfile);
            }
        }
    }
}
