package com.example.jobportal;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewJobPostActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private List<Data> jobPostList;
    private JobPostAdapter adapter;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_job_post);

        databaseReference = FirebaseDatabase.getInstance().getReference("Job Post");

        jobPostList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new JobPostAdapter(jobPostList);
        recyclerView.setAdapter(adapter);
        mDialog=new ProgressDialog(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobPostList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Data data = postSnapshot.getValue(Data.class);
                    jobPostList.add(data);
                }
                adapter.notifyDataSetChanged();
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.dismiss();
                // Handle database error
            }
        });
        mDialog.setMessage("Processing..");
        mDialog.show();

    }
}
