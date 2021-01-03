package com.efeyegitoglu.instagram.Profile;

public class KullaniciKayitModel {

    private String isim, mail, userName;

    public KullaniciKayitModel(String isim, String mail, String userNme) {
        this.isim=isim;
        this.mail = mail;
        this.userName = userNme;
    }

    public KullaniciKayitModel() {
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
