package com.google.sps.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Servlet responsible for deleting specified comment.
 * 
 * @author lucyqu
 *
 */
@WebServlet("/delete-comment")
public class DeleteCommentServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String commentEmail = request.getParameter("email");
    String userEmail = request.getParameter("user");
    //if user did not make the comment or if user is not logged in 
    if (!commentEmail.equals(userEmail) || !userService.isUserLoggedIn()){
      System.err.println("ERROR: Cannot delete comment.");
      return;
    }
    try {
      long id = Long.parseLong(request.getParameter("id"));
      Key commentEntityKey = KeyFactory.createKey("Comment", id);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.delete(commentEntityKey);
    } catch (NumberFormatException e){
      System.err.println("ERROR: Failed to parse to long.");
    }
  }
}