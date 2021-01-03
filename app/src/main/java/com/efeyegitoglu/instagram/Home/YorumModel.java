package com.efeyegitoglu.instagram.Home;

public class YorumModel {

    String gonderen, yorum;

    public YorumModel(String gonderen, String yorum) {
        this.gonderen = gonderen;
        this.yorum = yorum;
    }

    public YorumModel(){}

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }
}
