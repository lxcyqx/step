package com.google.sps.data;

public class Comment {
    long id;
    String text;
    String name;
    String timestamp;
    String email;

    public Comment(long id, String text, String name, String timestamp, String email){
        this.id = id;
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
        this.email = email;
    }
}