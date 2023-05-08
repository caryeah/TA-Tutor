package com.example.jobportal;

import static com.example.jobportal.StudentAdapter.downloadAndOpenPDF;
import static com.example.jobportal.StudentAdapter.jobApplications;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class AdapterPage extends RecyclerView.Adapter<AdapterPage.ViewHolder> {

    private List<JobApplication> personalInformationList;
    private Context mContext;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public AdapterPage(List<JobApplication> personalInformationList, Context context) {
        this.personalInformationList = personalInformationList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_update_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobApplication jobApplication = personalInformationList.get(position);

        holder.tvName.setText(jobApplication.getName());
        holder.tvSurname.setText(jobApplication.getSurname());
        holder.tvEmail.setText(jobApplication.getEmail());
        holder.tvStudentNumber.setText(jobApplication.getStudentNumber());
        holder.tvPhoneNumber.setText(jobApplication.getCellphone());

        holder.btnGetCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check that the context is not null
                if (v.getContext() != null) {
                    // Check for permission before downloading the file
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission not granted, request it
                        ActivityCompat.requestPermissions((Activity) v.getContext(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    } else {
                        // Permission granted, start download
                        downloadAndOpenPDF(v.getContext(), jobApplication.getPdfuri());
                    }
                }
            }
        });
    }

    private static void downloadAndOpenPDF(Context context, String pdfUrl) {
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

    @Override
    public int getItemCount() {
        return personalInformationList.size();
    }

    // Handle the permission request result
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start download
                    downloadAndOpenPDF(mContext, personalInformationList.get(0).getPdfuri());
                } else {
                    Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvSurname;
        private TextView tvEmail;
        private TextView tvStudentNumber;
        private TextView tvPhoneNumber;
        private Button btnGetCV;
        private TextView tvHeading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHeading = itemView.findViewById(R.id.tv_heading);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSurname = itemView.findViewById(R.id.tv_surname);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvStudentNumber = itemView.findViewById(R.id.tv_student_number);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            btnGetCV = itemView.findViewById(R.id.btn_get_cv);
        }
        }
    }
