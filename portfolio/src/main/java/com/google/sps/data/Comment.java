package com.google.sps.data;

public class Comment {
    long id;
    String text;
    String name;
    String timestamp;

    public Comment(long id, String text, String name, String timestamp){
        this.id = id;
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
    }
}