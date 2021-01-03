package com.efeyegitoglu.instagram.Home;

public class HomeModel {

    String comment,id, postid,resim;

    public HomeModel(String comment, String id, String postid, String resim) {
        this.comment = comment;
        this.id = id;
        this.postid=postid;
        this.resim = resim;
    }

    public HomeModel(){}

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }


}
