package com.example.jobportal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class RejectedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterPage adapterPage;
    private List<JobApplication> jobApplicationList;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected);
        recyclerView = findViewById(R.id.recycler_viewR);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobApplicationList = new ArrayList<>();
        adapterPage = new AdapterPage(jobApplicationList, this);
        recyclerView.setAdapter(adapterPage);
        mDialog=new ProgressDialog(this);



        databaseReference = FirebaseDatabase.getInstance().getReference("jobApplications");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobApplicationList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot applicationSnapshot : userSnapshot.getChildren()) {
                        JobApplication jobApplication = applicationSnapshot.getValue(JobApplication.class);
                        if (jobApplication != null && "rejected".equals(jobApplication.getStatus())) {
                            jobApplicationList.add(jobApplication);
                        }
                    }
                }

                adapterPage.notifyDataSetChanged();
                mDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                mDialog.dismiss();
            }
        });
        mDialog.setMessage("Processing..");
        mDialog.show();

    }
}
