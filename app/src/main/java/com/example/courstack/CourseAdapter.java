package com.example.courstack;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courstack.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    Context context;
    List<Course> courses;

    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = (Course) courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBackground;
        TextView tvCourseTitle;
        TextView tvCourseName;
        TextView tvInstructor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBackground = itemView.findViewById(R.id.ivCourseBackground);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvInstructor = itemView.findViewById(R.id.tvInstructorName);
        }

        public void bind(Course course) {
            tvCourseTitle.setText(course.getCourseTitle());
            tvCourseName.setText(course.getCourseName());
            tvInstructor.setText(course.getInstructor());
            Glide.with(context).load(course.getCourseBackground().getUrl()).into(ivBackground);
            ivBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("course", course);
                    context.startActivity(i);
                }
            });
        }
    }
}
