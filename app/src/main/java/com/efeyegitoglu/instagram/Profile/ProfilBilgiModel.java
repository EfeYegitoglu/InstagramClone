package com.efeyegitoglu.instagram.Profile;

public class ProfilBilgiModel {

    private String bio,cinsiyet,isim,telNo,website;

    public ProfilBilgiModel(String bio, String cinsiyet, String telNo, String website) {
        this.bio = bio;
        this.cinsiyet = cinsiyet;
        this.telNo = telNo;
        this.website = website;
    }

    public ProfilBilgiModel() {}

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
