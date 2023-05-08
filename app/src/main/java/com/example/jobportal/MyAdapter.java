package com.example.jobportal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<JobApplication> myDataList;
    private Context context;

    public MyAdapter(List<JobApplication> myDataList, Context context) {
        this.myDataList = myDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_a_r_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobApplication jobApplication = myDataList.get(position);

        holder.jobIdValueTextView.setText(jobApplication.getJobid());
        holder.nameValueTextView.setText(jobApplication.getName());
        holder.surnameValueTextView.setText(jobApplication.getSurname());
        holder.studentNumberValueTextView.setText(jobApplication.getStudentNumber());
        holder.phoneNumberValueTextView.setText(jobApplication.getCellphone());
        holder.statusValueTextView.setText(jobApplication.getStatus());
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobIdValueTextView, nameValueTextView, surnameValueTextView, studentNumberValueTextView, phoneNumberValueTextView, statusValueTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            jobIdValueTextView = itemView.findViewById(R.id.jobIdValueTextView);
            nameValueTextView = itemView.findViewById(R.id.nameValueTextView);
            surnameValueTextView = itemView.findViewById(R.id.surnameValueTextView);
            studentNumberValueTextView = itemView.findViewById(R.id.studentNumberValueTextView);
            phoneNumberValueTextView = itemView.findViewById(R.id.phoneNumberValueTextView);
            statusValueTextView = itemView.findViewById(R.id.statusValueTextView);
        }
    }
}
