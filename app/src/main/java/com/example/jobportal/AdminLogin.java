package com.example.jobportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class AdminLogin extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    Button buttonLogin;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        buttonLogin=findViewById(R.id.Admin_loginBtn);
        editTextEmail=findViewById(R.id.Admin_email);
        editTextPassword=findViewById(R.id.Admin_email);
        progressBar=findViewById(R.id.ProgressBar);
        textView=findViewById(R.id.LoginNow);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(view.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if (!email.equals("Admin") || !password.equals("Admin")) {
                    Toast.makeText(AdminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Intent intent= new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

        });
    }
}