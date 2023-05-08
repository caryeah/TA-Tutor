package com.example.jobportal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobportal.Model.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;


public class InsertJobPostActivity extends AppCompatActivity {

    private static final String TAG = "InsertJobPostActivity";

    private Toolbar toolbar;
    private EditText job_title;
    private EditText job_description;
    private EditText job_skills;
    private EditText job_salary;
    private Button btn_post_job;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mJobPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_job_post);

        toolbar = findViewById(R.id.insert_job_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Post job");


        mJobPost = FirebaseDatabase.getInstance().getReference().child("Job Post");

        InsertJob();
    }

    private void InsertJob() {
        job_title = findViewById(R.id.job_title);
        job_description = findViewById(R.id.job_Destriction);
        job_skills = findViewById(R.id.job_skills);
        job_salary = findViewById(R.id.job_salary);

        btn_post_job = findViewById(R.id.btn_job_post);

        btn_post_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = job_title.getText().toString().trim();
                String description = job_description.getText().toString().trim();
                String skills = job_skills.getText().toString().trim();
                String salary = job_salary.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    job_title.setError("Required...");
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    job_description.setError("Required...");
                    return;
                }
                if (TextUtils.isEmpty(skills)) {
                    job_skills.setError("Required...");
                    return;
                }
                if (TextUtils.isEmpty(salary)) {
                    job_salary.setError("Required...");
                    return;
                }

                String postId = mJobPost.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(title,description,skills,salary,date,postId);

                mJobPost.child(postId).setValue(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data was successfully uploaded
                                Toast.makeText(InsertJobPostActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), PostJobActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // There was an error uploading the data
                                Toast.makeText(InsertJobPostActivity.this, "upload failed", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }
}
