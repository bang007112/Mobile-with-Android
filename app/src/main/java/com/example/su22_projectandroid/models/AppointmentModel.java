package com.example.su22_projectandroid.models;


public class AppointmentModel {
    String ID, userID, hour, appointmentContent, date;
    UserModel userModel;

    public String getID() {return ID;}
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {this.userID = userID;}
    public UserModel userModel(){ return userModel;}

    public String getAppointmentContent() {
        return appointmentContent;
    }
    public void setAppointmentContent(String appointmentContent) {this.appointmentContent = appointmentContent;}

    public String getHour() {return hour;}
    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {return date;}
    public void setDate(String date) {
        this.date = date;
    }

    public UserModel getUserModel() {return userModel;}
    public void setHUserModel(String hour) {
        this.userModel = userModel;
    }

    public UserModel setUserModel() {return userModel;}
    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
