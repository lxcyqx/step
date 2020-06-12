// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentsInfo;

/**
 * Servlet responsible for displaying comment data.
 *
 * @author lucyqu
 *
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // By default comments ordered by timestamp
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    int currPage = Integer.parseInt(request.getParameter("page"));
    String languageCode = request.getParameter("languageCode");

    /*
     * Bet number of comments to show per page from user input and limit list of
     * comments depending on current page.
     */
    int numComments = this.getMaxNumComments(request);
    int offsetAmount;
    int nextPageOffset;
    /*
     * In the case that numComments is MAX_VALUE, multiplying it by even number will
     * result in negative number and offset cannot be negative.
     */
    if (numComments * currPage < 0) {
      offsetAmount = Integer.MAX_VALUE;
      nextPageOffset = Integer.MAX_VALUE;
    } else {
      offsetAmount = numComments * currPage;
      nextPageOffset = numComments * (currPage + 1);
    }

    List<Entity> limitedComments = results
        .asList(FetchOptions.Builder.withLimit(numComments).offset(offsetAmount));
    List<Entity> nextPageComments = results
        .asList(FetchOptions.Builder.withLimit(numComments).offset(nextPageOffset));

    // Add entity to list of comments
    List<Comment> commentsList = new ArrayList<Comment>();
    for (Entity entity : limitedComments) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty("text");
      String timestamp = (String) entity.getProperty("timestamp");
      String email = (String) entity.getProperty("email");
      Comment comment = new Comment(id, text, timestamp, email);
      commentsList.add(comment);
    }
    CommentsInfo commentsInfo = new CommentsInfo(commentsList, nextPageComments.size());
    String json = new Gson().toJson(commentsInfo);

    // Send JSON as response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Gets number of comments user wants to display per page of comments.
   *
   * @param HTTP request
   * @return number of comments to be displayed
   */
  private int getMaxNumComments(HttpServletRequest request) {
    String numCommentsString = request.getParameter("num");
    if (numCommentsString.equals("5")) {
      return 5;
    } else if (numCommentsString.equals("10")) {
      return 10;
    } else if (numCommentsString.equals("20")) {
      return 20;
    } else {
      return Integer.MAX_VALUE;
    }
  }
}
