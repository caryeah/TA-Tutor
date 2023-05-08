package com.example.jobportal;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Data> jobList;

    public JobAdapter(List<Data> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_post_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Data data = jobList.get(position);
        holder.jobTitle.setText(data.getTitle());
        holder.jobDescription.setText(data.getDescription());
        holder.skills.setText(data.getSkills());
        holder.salary.setText(data.getSalary());
        holder.dateJob.setText(data.getDate());
        holder.applyButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ApplyingForJob.class);
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView title, jobTitle, jobDescription, skills, salary, dateJob;
        Button applyButton, cancelButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            jobTitle = itemView.findViewById(R.id.job_title);
            jobDescription = itemView.findViewById(R.id.job_description);
            skills = itemView.findViewById(R.id.skills);
            salary = itemView.findViewById(R.id.salary);
            dateJob = itemView.findViewById(R.id.DateJob);
            applyButton = itemView.findViewById(R.id.apply_button);
        }
    }
}
