package com.tttn.model;

import java.util.Date;
import java.util.List;

public class LichLamModel {
    private int caID;
    private String workday, idUser;
    public  LichLamModel(){

    }

    public LichLamModel( String workday,int caID, String idUser) {
        this.caID = caID;
        this.workday = workday;
        this.idUser = idUser;
    }

    public int getCaID() {
        return caID;
    }

    public void setCaID(int caID) {
        this.caID = caID;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
