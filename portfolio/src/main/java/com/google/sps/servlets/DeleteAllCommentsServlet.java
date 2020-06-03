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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/delete-data")
public class DeleteAllCommentsServlet extends HttpServlet {
  
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //get comment from input box
        String comment = request.getParameter("comment-box");
        this.comments.add(comment);
        // response.setContentType("text/html");
        // response.getWriter().println(comment);

        //create comment entity
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", comment);

        //store entity in database
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

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
