package com.efeyegitoglu.instagram.Search;

public class AraModel {

    java.lang.String isim, resim, userName, id;

    public AraModel(java.lang.String isim, java.lang.String resim, java.lang.String userName, String id) {
        this.isim = isim;
        this.resim = resim;
        this.userName = userName;
        this.id = id;
    }

    public AraModel() {
    }

    public java.lang.String getIsim() {
        return isim;
    }

    public void setIsim(java.lang.String isim) {
        this.isim = isim;
    }

    public java.lang.String getResim() {
        return resim;
    }

    public void setResim(java.lang.String resim) {
        this.resim = resim;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
