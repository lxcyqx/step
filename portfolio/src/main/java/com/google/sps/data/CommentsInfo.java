package com.google.sps.data;

import java.util.List;

/**
 * Contains information for comment section. Holds a list of comments to be
 * displayed on current page and number of comments on next page to determine
 * whether current page is the last page.
 *
 * @author lucyqu
 *
 */
public class CommentsInfo {

  // list of comments on page
  public final List<Comment> commentsList;
  // number of comments on next page
  public final int numComments;

  /**
   * Constructor that sets instance variables holding arraylist of comments and
   * integer of number of comments on next page.
   *
   * @param commentsList List of Comment objects
   * @param numComments  integer number of comments on next page
   */
  public CommentsInfo(List<Comment> commentsList, int numComments) {
    this.commentsList = commentsList;
    this.numComments = numComments;
  }
}
