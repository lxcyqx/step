package com.google.sps.data;

public class Comment {
    private final long id;
    private final String text;
    private final String timestamp;
    private final String email;

    public Comment(long id, String text, String timestamp, String email){
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.email = email;
    }
}