package com.example.courstack.ui.classmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.R;
import com.example.courstack.models.Student;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<ParseUser> students;

    public StudentAdapter(Context context, List<ParseUser> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser student = students.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvMajor;
        private TextView tvBio;
        private ImageView ivHead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvMajor = itemView.findViewById(R.id.tvMajor);
            tvBio = itemView.findViewById(R.id.tvBio);
            ivHead =itemView.findViewById(R.id.ivHead);
        }

        public void bind(ParseUser student) {
            tvUsername.setText(student.getString("username"));
            tvMajor.setText(student.getString("major"));
            tvBio.setText(student.getString("description"));
            ParseFile image = student.getParseFile("profile_image");
            if (image != null) {
                Glide.with(context).load(student.getParseFile("profile_image").getUrl()).into(ivHead);
            }
        }
    }
}
