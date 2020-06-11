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
import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

/**
 * Handles user email authentication for the comment section.
 *
 * @author lucyqu
 *
 */
@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {

  static final String REDIRECTION_URL = "/videos.html#comment-box";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    HashMap<String, String> responseMap = new HashMap<>();

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      final String userEmail = userService.getCurrentUser().getEmail();
      final String logoutUrl = userService.createLogoutURL(REDIRECTION_URL);

      responseMap.put("email", userEmail);
      responseMap.put("isLoggedIn", "true");
      responseMap.put("logoutUrl", logoutUrl);

    } else {
      final String loginUrl = userService.createLoginURL(REDIRECTION_URL);

      responseMap.put("isLoggedIn", "false");
      responseMap.put("loginUrl", loginUrl);
    }

    Gson gson = new Gson();
    String json = gson.toJson(responseMap);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
