package com.example.jobportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserHome extends AppCompatActivity {
    Button buttonLog_ViewJobs,buttonLogout,buttonMyJobs,buttonView;
    FirebaseUser user;
    FirebaseAuth auth;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        auth= FirebaseAuth.getInstance();
        buttonLog_ViewJobs=findViewById(R.id.JobPost);
        buttonMyJobs=findViewById(R.id.JobsApplied);
        buttonLogout=findViewById(R.id.log_out);
        textView=findViewById(R.id.username_text);
        user=auth.getCurrentUser();
        if(user==null){
            Intent intent= new Intent(getApplicationContext(),RegistractionActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail());
        }

        buttonLog_ViewJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),AllJobActivity.class);
                startActivity(intent);
                finish();

            }
        });
        buttonMyJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),UserUpdateActivity.class);
                startActivity(intent);
                finish();

            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }

        });

    }
}