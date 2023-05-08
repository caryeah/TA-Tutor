package com.example.jobportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class ApplyingForJob extends AppCompatActivity {
    private EditText nameInput, surnameInput, studentNumberInput, cellphoneInput, jobInput;
    private Button uploadCvButton, sendButton, homeButton;
    private static final int REQUEST_CODE_PICK_PDF = 100;
    private ProgressBar progressBar;
    private Uri pdfUri;
    private DatabaseReference databaseRef;
    private String pdfUrl = "";
    private ProgressDialog mDialog;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applying_for_job);

        nameInput = findViewById(R.id.name_input);
        surnameInput = findViewById(R.id.surname_input);
        studentNumberInput = findViewById(R.id.student_number_input);
        cellphoneInput = findViewById(R.id.cellphone_input);
        jobInput = findViewById(R.id.Job_ID);
        uploadCvButton = findViewById(R.id.upload_cv_button);
        sendButton = findViewById(R.id.send_button);
        homeButton = findViewById(R.id.Back_To_Home_button);

        mDialog=new ProgressDialog(this);
        // Initialize progress bar visibility to GONE

        databaseRef = FirebaseDatabase.getInstance().getReference(); // initialize databaseRef

        homeButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserHome.class)));

        uploadCvButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_CODE_PICK_PDF);
        });

        sendButton.setOnClickListener(view -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String uid = currentUser.getUid();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                mDialog.setMessage("Uploading Application..");
                mDialog.show();

                StorageReference storageRef = storage.getReference().child("pdfs/" + UUID.randomUUID().toString() + ".pdf");
                if (currentUser.getEmail()!=null){
                    userEmail=currentUser.getEmail();
                }
                UploadTask uploadTask = storageRef.putFile(pdfUri);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        pdfUrl = uri.toString();
                        String applicationKey = databaseRef.child("jobApplications").child(uid).push().getKey();
                        JobApplication jobApplication = new JobApplication(nameInput.getText().toString(), surnameInput.getText().toString(), studentNumberInput.getText().toString(), cellphoneInput.getText().toString(), pdfUrl, uid, jobInput.getText().toString(),userEmail,applicationKey);
                        jobApplication.setStatus("no action");
                        // Create the "application" node with a unique key
                        databaseRef.child("jobApplications").child(uid).child(applicationKey).setValue(jobApplication)

                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ApplyingForJob.this, "Job Application Sent Successfully", Toast.LENGTH_SHORT).show();
                                    sendEmailNotification(nameInput.getText().toString(), surnameInput.getText().toString(), userEmail,jobInput.getText().toString());
                                    mDialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ApplyingForJob.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                });
                    });
                });
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void sendEmailNotification(String name, String surname, String email, String JobId) {
        // Create a new thread to send the email
        new Thread(() -> {
            try {
                // Set up the mail properties
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Create a new session with authentication
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("siphid08@gmail.com", "xjehchbxjezpqmor");
                    }
                });

                // Create a new message
                Message message = new MimeMessage(session);

                // Set the sender address
                message.setFrom(new InternetAddress("dutschoolcodingproject@gmail.com"));

                // Set the recipient address
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

                // Set the subject and body
                message.setSubject("Job Application Submitted");
                message.setText("Dear " + name + " " + surname + ",\n\n" +
                        "Your job application for the "+ JobId +"has been successfully submitted. "+
                        "Please monitor the app and your emails for a response.\n\n" +
                        "Best regards,\n" +
                        "Dut Team");

                // Send the message
                Transport.send(message);
                // Log success message
                Log.i("Email", "Email sent successfully");

            } catch (Exception e) {
                // Log error message
                Log.e("Email", "Error sending email: " + e.getMessage(), e);
            }
        }).start();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PDF && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
        }
    }
}

