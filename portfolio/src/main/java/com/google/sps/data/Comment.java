package com.google.sps.data;

public class Comment {
    private final long id;
    private final String text;
    private final String name;
    private final String timestamp;
    private final String email;

    public Comment(long id, String text, String name, String timestamp, String email){
        this.id = id;
        this.text = text;
        this.name = name;
        this.timestamp = timestamp;
        this.email = email;
    }
}