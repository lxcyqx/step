package com.google.sps.data;

public class Comment {
    long id;
    String text;
    String name;
    String timestamp;

    public Comment(String text, String name, String timestamp){
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
    }
}