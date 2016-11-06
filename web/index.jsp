<%@ page import="java.lang.reflect.Array" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: BigRockerEnding
  Date: 10/30/16
  Time: 1:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
      <link type="text/css" rel="stylesheet" href="stylesheets/main.css"/>
    <title>$Title$</title>
  </head>
  <body>
  <%
      String guestbookName = request.getParameter("guestbookName");
      if (guestbookName == null) guestbookName = "default";
      pageContext.setAttribute("guestbookName", guestbookName);
      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();
      if (user != null) {
          pageContext.setAttribute("user", user);
  %>
  <p>Hello, ${fn:escapeXml(user.nickname)}! (You can
      <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
  <%
  } else {
  %>
  <p>Hello!
      <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
      to include your name with greetings you post.</p>
  <%
      }
  %>
  </body>
</html>
