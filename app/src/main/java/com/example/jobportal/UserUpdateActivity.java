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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserUpdateActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Query databaseReference;
    private ProgressDialog mDialog;
    private List<JobApplication> jobApplicationList;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        RecyclerView recyclerView = findViewById(R.id.recycler_vieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobApplicationList = new ArrayList<>();
        adapter = new MyAdapter(jobApplicationList, this);
        recyclerView.setAdapter(adapter);
        mDialog=new ProgressDialog(this);

        progressBar = findViewById(R.id.progress_circlee);

        // Get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get the current user's ID
            databaseReference= FirebaseDatabase.getInstance().getReference("jobApplications").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    jobApplicationList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        JobApplication jobApplication = dataSnapshot.getValue(JobApplication.class);
                        jobApplicationList.add(jobApplication);
                    }
                    adapter.notifyDataSetChanged();
                    mDialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error fetching data", error.toException());
                    mDialog.dismiss();

                }
            });
            mDialog.setMessage("Processing..");
            mDialog.show();


        }
    }
}
