package com.example.jobportal;


public class Data {
    String title;
    String description;
    String skills;
    String salary;

    String date;
    String id;
    public Data(){

    }

    public Data(String title, String description, String skills, String salary, String date, String id){

            this.title = title;
            this.description = description;
            this.skills = skills;
            this.salary = salary;
            this.date = date;
            this.id=id;



    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSkills() {
        return skills;
    }

    public String getSalary() {
        return salary;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
