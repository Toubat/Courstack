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

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<Student> students;

    public StudentAdapter(Context context, List<Student> students) {
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
        Student student = students.get(position);
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
        }

        public void bind(Student student) {
            tvUsername.setText(student.getUsername());
            tvBio.setText(student.getDescription());
            ParseFile image = student.getImage();
            if (image != null) {
                Glide.with(context).load(student.getImage().getUrl()).into(ivHead);
            }
        }
    }
}
