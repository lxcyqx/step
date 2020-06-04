package com.google.sps.data;

public class Comment {
    String text;
    String name;
    long timestamp;

    public Comment(String text, String name, long timestamp){
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
    }
}