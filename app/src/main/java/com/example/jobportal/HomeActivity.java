package com.example.jobportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
    private Button btnAlljob;
    private Button btnPostjob,AllAplicationsbtn,Acepted,Rejected;

    //toolbar

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Job Portal App");

        btnAlljob=findViewById(R.id.btn_alljob);
        btnPostjob=findViewById(R.id.btn_Postjob);
        AllAplicationsbtn=findViewById(R.id.btn_Applications);
        Acepted=findViewById(R.id.btn_Hired_Applications);
        Rejected=findViewById(R.id.btn_Rejected_Applications);
        Rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RejectedActivity.class));
            }
        });
        Acepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AcceptedActivity.class));
            }
        });
        AllAplicationsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewAplicants.class));
            }
        });

        btnAlljob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),AdminViewJobPostActivity.class));

            }
        });

        btnPostjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PostJobActivity.class));

            }
        });
    }

    public void setSupportActionBar(Toolbar toolbar) {
    }
}