package com.efeyegitoglu.instagram.Profile;

public class ProfileResmiModel {

    String id,isim, resim, userName;

    public ProfileResmiModel(String id, String isim, String resim, String userName) {
        this.id=id;
        this.isim = isim;
        this.resim = resim;
        this.userName = userName;
    }

    public ProfileResmiModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
