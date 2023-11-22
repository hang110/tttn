package com.tttn.model;

public class ChamCongModel {
    private String idUser,date,time_CI, time_CO, image_CI, image_CO;

    public ChamCongModel() {
    }

    public ChamCongModel(String idUser, String date, String time_CI, String time_CO, String image_CI, String image_CO) {
        this.idUser = idUser;
        this.date = date;
        this.time_CI = time_CI;
        this.time_CO = time_CO;
        this.image_CI = image_CI;
        this.image_CO = image_CO;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_CI() {
        return time_CI;
    }

    public void setTime_CI(String time_CI) {
        this.time_CI = time_CI;
    }

    public String getTime_CO() {
        return time_CO;
    }

    public void setTime_CO(String time_CO) {
        this.time_CO = time_CO;
    }

    public String getImage_CI() {
        return image_CI;
    }

    public void setImage_CI(String image_CI) {
        this.image_CI = image_CI;
    }

    public String getImage_CO() {
        return image_CO;
    }

    public void setImage_CO(String image_CO) {
        this.image_CO = image_CO;
    }
}
