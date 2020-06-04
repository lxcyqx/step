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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.gson.Gson;
import com.google.sps.data.Comment;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
    private List<Comment> comments;

    /** Add comments to Arraylist*/
    @Override
    public void init() {
        comments = new ArrayList<Comment>();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //by default comments ordered by timestamp
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        //get number of max comments to show from user input and limit list of comments
        int numComments = this.getMaxNumComments(request);
        List<Entity> limitedComments = results.asList(FetchOptions.Builder.withLimit(numComments));
        
        //add entity to list of comments
        List<Comment> commentsList = new ArrayList<Comment>();
        for (Entity entity : limitedComments){
            String text = (String) entity.getProperty("text");
            String name = (String) entity.getProperty("name");
            long timestamp = (long) entity.getProperty("timestamp");
            Comment comment = new Comment(text, name, timestamp);
            commentsList.add(comment);
        }

        String json = new Gson().toJson(commentsList);

        //Send JSON as response
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //get comment from input box along with name and timestamp
        String text = request.getParameter("comment-box");        
        String name = request.getParameter("name-box");
        long timestamp = System.currentTimeMillis();

        //if user entered a comment
        if (!text.equals("")){
            Comment comment = new Comment(text, name, timestamp);
            this.comments.add(comment);

            //create comment entity
            Entity commentEntity = new Entity("Comment");
            commentEntity.setProperty("text", text);
            commentEntity.setProperty("name", name);
            commentEntity.setProperty("timestamp", timestamp);

            //store entity in database
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(commentEntity);
        }
        //redirect to HTML page
        response.sendRedirect("/videos.html#comment-box");
    }

    /** 
    * Get number of comments user wants to display.
    *
    * @param HTTP request
    * @return number of comments to be displayed
    */
    private int getMaxNumComments(HttpServletRequest request){
        String numCommentsString = request.getParameter("num");
        if (numCommentsString.equals("5")){
            return 5;
        } else if (numCommentsString.equals("10")){
            return 10;
        } else if (numCommentsString.equals("20")){
            return 20;
        } else {
            return Integer.MAX_VALUE;
        }
    }
}
