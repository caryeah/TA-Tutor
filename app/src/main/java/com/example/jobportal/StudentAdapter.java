package com.example.jobportal;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    static List<JobApplication> jobApplications;
    private static DatabaseReference databaseRef;
    private static final int REQUEST_CODE = 1;
    private Context mContext;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public StudentAdapter(Context context) {
        mContext = context;
    }

    public StudentAdapter(List<JobApplication> jobApplications, DatabaseReference databaseRef) {
        this.jobApplications = jobApplications;
        this.databaseRef = databaseRef;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newapplicant_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JobApplication jobApplication = jobApplications.get(position);
        holder.nameTextView.setText("Name: " + jobApplication.getName());
        holder.surnameTextView.setText("Surname: " + jobApplication.getSurname());
        holder.studentNumberTextView.setText("Student Number: " + jobApplication.getStudentNumber());
        holder.cellphoneTextView.setText("Cellphone: " + jobApplication.getCellphone());
        holder.JobId.setText("Job Title: " + jobApplication.getJobid());

        holder.rejectButton.setOnClickListener(view -> {
            String uid = jobApplication.getUid();
            String jobapplicationId = jobApplication.getJobApplicationId();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("jobApplications").child(uid).child(jobapplicationId);
            String name=jobApplication.getName();
            String surname=jobApplication.getSurname();
            String email=jobApplication.getEmail();
            String job=jobApplication.getJobid();

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        JobApplication application = snapshot.getValue(JobApplication.class);
                        if (application != null && "no action".equals(application.getStatus())) {
                            application.setStatus("rejected");
                            Map<String, Object> updateValues = new HashMap<>();
                            updateValues.put("status", "rejected");

                            userRef.updateChildren(updateValues)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(holder.itemView.getContext(), "Application rejected!", Toast.LENGTH_SHORT).show();
                                        sendEmailNotificationR(name, surname, email,job);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(holder.itemView.getContext(), "Failed to reject application", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Application has already been approved or rejected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Application not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error fetching data", error.toException());
                }
            });
        });



        holder.approveButton.setOnClickListener(view -> {
            String uid = jobApplication.getUid();
            String jobapplicationId= jobApplication.getJobApplicationId();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("jobApplications").child(uid).child(jobapplicationId);
            String name=jobApplication.getName();
            String surname=jobApplication.getSurname();
            String email=jobApplication.getEmail();
            String job=jobApplication.getJobid();


            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        JobApplication application = snapshot.getValue(JobApplication.class);
                        if (application != null && "no action".equals(application.getStatus())) {
                            application.setStatus("approved");
                            Map<String, Object> updateValues = new HashMap<>();
                            updateValues.put("status", "approved");

                            userRef.updateChildren(updateValues)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(holder.itemView.getContext(), "Application approved!", Toast.LENGTH_SHORT).show();
                                        sendEmailNotification(name, surname, email,job);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(holder.itemView.getContext(), "Failed to approve application", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Application has already been approved or rejected", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Application not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error fetching data", error.toException());
                }
            });
        });


        holder.Cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check that the context is not null
                if (view.getContext() != null) {
                    // Check for permission before downloading the file
                    if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission not granted, request it
                        ActivityCompat.requestPermissions((Activity) view.getContext(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    } else {
                        // Permission granted, start download
                        downloadAndOpenPDF(view.getContext(), jobApplication.getPdfuri());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobApplications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView, surnameTextView, studentNumberTextView, cellphoneTextView, JobId;
        public Button approveButton, rejectButton, Cv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            surnameTextView = itemView.findViewById(R.id.surnameTextView);
            studentNumberTextView = itemView.findViewById(R.id.studentNumberTextView);
            cellphoneTextView = itemView.findViewById(R.id.cellphoneTextView);
            JobId = itemView.findViewById(R.id.JobId);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            Cv = itemView.findViewById(R.id.Cv);

            approveButton.setOnClickListener(this);
            rejectButton.setOnClickListener(this);
            Cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            JobApplication jobApplication = jobApplications.get(position);

            switch (view.getId()) {
                case R.id.approveButton:
                    // Perform the approve action
                    String key = jobApplication.getKey();
                    String jobid = jobApplication.getUid();
                    databaseRef.child(jobid).child(key).child("status").setValue("approved");
                    Toast.makeText(view.getContext(), "Application approved", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rejectButton:
                    // Perform the reject action
                    Log.d(TAG, "Reject button clicked");


                    if (jobApplication == null) {
                        Log.e(TAG, "jobApplication is null");
                        return;
                    }
                    Log.d(TAG, "jobApplication: " + jobApplication.toString());
                    String jobiD = jobApplication.getUid(); // get the jobid (which is assumed to be the user's uid)
                    String applicationKey = jobApplication.getKey(); // get the key of the job application
                    Log.d(TAG, "jobiD: " + jobiD + ", applicationKey: " + applicationKey);
                    DatabaseReference applicationRef = FirebaseDatabase.getInstance().getReference("jobApplications").child(jobiD).child(applicationKey);
                    applicationRef.child("status").setValue("rejected");
                    Toast.makeText(view.getContext(), "Application rejected", Toast.LENGTH_SHORT).show();
                    break;


                case R.id.Cv:
// Check that the context is not null
                    if (view.getContext() != null) {
// Check for permission before downloading the file
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
// Permission not granted, request it
                            ActivityCompat.requestPermissions((Activity) view.getContext(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
// Permission granted, start download
                            downloadAndOpenPDF(view.getContext(), jobApplication.getPdfuri());
                        }
                    }
                    break;
            }
        }
    }

    static void downloadAndOpenPDF(Context context, String pdfUrl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("CV download");
        request.setDescription("Downloading CV");

        // This method will notify when the file is downloaded and ready to open
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                File downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/CV.pdf");
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(downloadedFile), "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent1 = Intent.createChooser(target, "Open File");
                try {
                    context.startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_LONG).show();
                }
                context.unregisterReceiver(this);
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start download
                    downloadAndOpenPDF(mContext, jobApplications.get(0).getPdfuri());
                } else {
                    Toast.makeText(mContext, "Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
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
                message.setSubject("Job Application Status");
                message.setText("Dear " + name + " " + surname + ",\n\n" +
                        "Congratulations! We are pleased to inform you that you have been selected for the " + JobId + " position. " +
                        "We are excited to have you on board and look forward to working with you. We will be in contact with you shortly " +
                        "for more information regarding your start date and other details.\n\n" +
                        "Thank you for choosing to work with us.\n\n" +
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
    private void sendEmailNotificationR(String name, String surname, String email, String JobId) {
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
                message.setSubject("Job Application Status");
                message.setText("Dear " + name + " " + surname + ",\n\n" +
                        "Thank you for your interest in the " + JobId + " position. We appreciate the time and effort you put into your application." +
                        " Unfortunately, we regret to inform you that we have decided not to proceed with your application at this time. " +
                        "However, we would like to keep your CV on file for any future job opportunities that may arise.\n\n" +
                        "Thank you again for considering us as a potential employer.\n\n" +
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
}



