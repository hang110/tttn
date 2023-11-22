package com.tttn.model;

public class UserModel {
    String address, name, tele, idUser;

    public UserModel() {
    }

    public UserModel(String address, String name, String tele, String idUser) {
        this.address = address;
        this.name = name;
        this.tele = tele;
        this.idUser = idUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
