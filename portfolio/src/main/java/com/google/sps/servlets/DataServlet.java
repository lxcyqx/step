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
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;
import java.util.Date;
import java.lang.Math;
import java.text.SimpleDateFormat; 
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.gson.Gson;
import com.google.sps.data.Comment;

/** Servlet responsible for displaying comment data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //by default comments ordered by timestamp
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        int currPage = Integer.parseInt(request.getParameter("page"));
        String languageCode = request.getParameter("languageCode");

        /* get number of comments to show per page from user input and limit list of comments depending on current page */
        int numComments = this.getMaxNumComments(request);
        int offsetAmount;
        /* in the case that numComments is MAX_VALUE, multiplying it by even number will result in negative number and offset cannot be negative */
        if (numComments*currPage < 0){
            offsetAmount = Integer.MAX_VALUE;
        } else {
            offsetAmount = numComments*currPage;
        }
        
        List<Entity> limitedComments = limitedComments = results.asList(FetchOptions.Builder.withLimit(numComments).offset(offsetAmount));
        
        //add entity to list of comments
        List<Comment> commentsList = new ArrayList<Comment>();
        for (Entity entity : limitedComments){
            long id = entity.getKey().getId();
            String text = (String) entity.getProperty("text");
            String name = (String) entity.getProperty("name");
            String timestamp = (String) entity.getProperty("timestamp");
            Comment comment = new Comment(id, text, name, timestamp);
            commentsList.add(comment);
        }

        String json = new Gson().toJson(commentsList);

        //Send JSON as response
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    /** 
    * Get number of comments user wants to display per page of comments.
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
