package com.google.sps.data;

/**
 * Holds data regarding each comment.
 *
 * @author lucyqu
 *
 */
public class Comment {
  private final long id;
  private final String text;
  private final String timestamp;
  private final String email;

  /**
   * Constructor to set instance variables.
   * 
   * @param id        comment id
   * @param text      comment text
   * @param timestamp time at which comment is posted
   * @param email     user email
   */
  public Comment(long id, String text, String timestamp, String email) {
    this.id = id;
    this.text = text;
    this.timestamp = timestamp;
    this.email = email;
  }
}