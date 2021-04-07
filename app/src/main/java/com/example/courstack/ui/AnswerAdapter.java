package com.example.courstack.ui;

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
import com.example.courstack.models.Answer;
import com.parse.ParseFile;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    public static final int BRIEF = 1;
    public static final int WHOLE = 0;

    Context context;
    List<Answer> answers;
    int type;

    public AnswerAdapter(Context context, List<Answer> answers, int type) {
        this.context = context;
        this.answers = answers;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == WHOLE) {
            View view = inflater.inflate(R.layout.whole_item_answer, parent, false);
            return new WholeAnswerViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.brief_item_answer, parent, false);
            return new BriefAnswerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);
        if (holder.getItemViewType() == WHOLE) {
            WholeAnswerViewHolder viewHolder = (WholeAnswerViewHolder) holder;
            viewHolder.bind(answer);
        } else {
            BriefAnswerViewHolder viewHolder = (BriefAnswerViewHolder) holder;
            viewHolder.bind(answer);
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public abstract class AnswerViewHolder extends RecyclerView.ViewHolder {

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(Answer answer);
    }

    public class BriefAnswerViewHolder extends AnswerViewHolder {

        ImageView ivProfile;
        TextView tvDescription;

        public BriefAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivSmallProfile);
            tvDescription = itemView.findViewById(R.id.tvBriefAnswer);
        }

        @Override
        public void bind(Answer answer) {
            tvDescription.setText(answer.getText());
            ParseFile profile = answer.getStudent().getParseFile("profile_image");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).into(ivProfile);
            }
        }
    }

    public class WholeAnswerViewHolder extends AnswerViewHolder {

        ImageView ivProfile;
        TextView tvUsername;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvLastUpdate;

        public WholeAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUsername = itemView.findViewById(R.id.tvUsernameForumPost);
            tvTitle = itemView.findViewById(R.id.tvAnswerTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate);
        }

        @Override
        public void bind(Answer answer) {
            tvUsername.setText(answer.getStudent().getUsername());
            tvTitle.setText(answer.getTitle());
            tvDescription.setText(answer.getText());
            tvLastUpdate.setText((CharSequence) answer.getUpdatedAt());
            ParseFile profile = answer.getStudent().getParseFile("profile_image");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).into(ivProfile);
            }
        }
    }
}
