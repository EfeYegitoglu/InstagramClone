package com.efeyegitoglu.instagram.Message;

public class SendMessageModel {
    private String from,text;

    public SendMessageModel(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public SendMessageModel(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MesajModel{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
