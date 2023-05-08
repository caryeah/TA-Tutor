package com.example.jobportal;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;



public class NewAplicants extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<JobApplication> jobApplications;

    // Add reference to Firebase database
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_aplicants);

        // Initialize jobApplications list and Firebase database reference
        jobApplications = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Retrieve data from Firebase database
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobApplications.clear();

                DataSnapshot jobApplicationsSnapshot = dataSnapshot.child("jobApplications");
                for (DataSnapshot userSnapshot : jobApplicationsSnapshot.getChildren()) {
                    for (DataSnapshot applicationSnapshot : userSnapshot.getChildren()) {
                        if (applicationSnapshot.exists()) {
                            JobApplication jobApplication = applicationSnapshot.getValue(JobApplication.class);
                            if (jobApplication != null && "no action".equals(jobApplication.getStatus())) {
                                jobApplications.add(jobApplication);
                            }
                        }
                    }
                }

                // Notify adapter of data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("NewApplicants", "Error retrieving job applications from Firebase", databaseError.toException());
            }
        });

        // Set up RecyclerView and adapter
        recyclerView = findViewById(R.id.New_Application_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(jobApplications, databaseRef);
        recyclerView.setAdapter(adapter);
    }
}




