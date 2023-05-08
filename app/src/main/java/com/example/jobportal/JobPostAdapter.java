package com.example.jobportal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {

    private List<Data> jobPostList;

    public JobPostAdapter(List<Data> jobPostList) {
        this.jobPostList = jobPostList;
    }

    @NonNull
    @Override
    public JobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post_card_view, parent, false);
        return new JobPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobPostViewHolder holder, int position) {
        Data data = jobPostList.get(position);
        holder.jobTitle.setText(data.getTitle());
        holder.jobDescription.setText(data.getDescription());
        holder.jobSkills.setText(data.getSkills());
        holder.jobSalary.setText(data.getSalary());
        holder.btnDeleteJobPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobId = data.getId();
                DatabaseReference jobPostRef = FirebaseDatabase.getInstance().getReference("Job Post").child(jobId);
                jobPostRef.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobPostList.size();
    }

    static class JobPostViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle;
        TextView jobDescription;
        TextView jobSkills;
        TextView jobSalary;
        Button btnDeleteJobPost;

        public JobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            jobDescription = itemView.findViewById(R.id.job_description);
            jobSkills = itemView.findViewById(R.id.job_skills);
            jobSalary = itemView.findViewById(R.id.job_salary);
            btnDeleteJobPost = itemView.findViewById(R.id.btn_delete_job_post);
        }
    }
}
