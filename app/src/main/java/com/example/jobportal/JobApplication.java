package com.example.jobportal;

import com.google.firebase.database.Exclude;

public class JobApplication {
    private String name;
    private String surname;
    private String studentNumber;
    private String cellphone;
    private String pdfuri;
    private String uid;
    private String jobid;
    private String mstatus;
    private String email;
    private String mkey;
    private String jobApplicationId;

    public JobApplication() {
        // Required empty constructor for Firebase
    }

    public JobApplication(String name, String surname, String studentNumber, String cellphone,String pdfuri,String uid,String jobid,String email,String jobApplicationId) {
        this.name = name;
        this.surname = surname;
        this.studentNumber = studentNumber;
        this.cellphone = cellphone;
        this.pdfuri=pdfuri;
        this.uid = uid;
        mstatus="no action";
        this.jobid=jobid;
        this.email=email;
        this.jobApplicationId=jobApplicationId;
    }
    public String getJobApplicationId(){return jobApplicationId;}
    public void setJobApplicationId(String jobApplicationId){this.jobApplicationId=jobApplicationId;}
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getStatus(){
        return mstatus;
    }
    public void setStatus(String status){
        mstatus=status;
    }
    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getPdfuri() {
        return pdfuri;
    }

    public void setPdfuri(String pdfuri) {
        this.pdfuri = pdfuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    @Exclude
    public String getKey(){return mkey;}
    @Exclude
    public void setKey(String Key){mkey=Key;}
}
