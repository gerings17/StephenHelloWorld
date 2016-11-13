Introduction to Persistence in Java Servlets
============================================
At this point your hello world app should have a functioning JSP page. We will use it as we move forward to understand how to store Java objects in a database so we can reuse them.

**Prerequisite:** Prior to beginning this recipe you should have a HelloWorld project that has a guestbook.jsp file that demonstrates the use of the user service. If your project currently uses index.jsp rename it. You may also want to add the following to your web.xml, this will open your jsp file when you browse to http://localhost:8080.
```xml
<welcome-file-list>
    <welcome-file>guestbook.jsp</welcome-file>
</welcome-file-list>
```

1. Add the following dependencies to your pom.xml. This will make these classes available in your project via Maven.

    ```xml
    <!-- https://mvnrepository.com/artifact/com.googlecode.objectify/objectify -->
    <dependency>
        <groupId>com.googlecode.objectify</groupId>
        <artifactId>objectify</artifactId>
        <version>5.1.13</version>
    </dependency>
    
    <!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>20.0</version>
    </dependency>
    ```
    
2. Create our first business class: `Greeting.java`
    1. Annotate the class as an `@Entity`
    2. Add the following fields, don’t worry about the annotations (@) for now but import them from com.googlecode.objectify.annotation not javax.persistence:


        ```java
            @Parent Key<Guestbook> theBook;
            @Id public Long id;
            public String author_email;
            public String author_id;
            public String content;
            @Index public Date date;
        ```

    3. Make a constructor with no arguments set the current date. (Try using alt-insert.)
    4. Make a constructor that takes Strings for book and content. Call the default constructor and then set the content field. Use the following code to create a guestbook key from your book parameter.
    
        ```java
        if( book != null ) {
          theBook = Key.create(Guestbook.class, book);  // Creating the Ancestor key
        } else {
          theBook = Key.create(Guestbook.class, "default");
        }
        ```
        
    5. Make a third constructor that takes a book, the content and the author’s email and id. Call the previous constructor, then set the author fields.
6. Create our second business class: `Guestbook.java`
    3. Add a `public String book`.
    4. Annotate the book as an `@Id`
    5. Annotate the class as an `@Entity`
7. Create an objectifiy helper class: `OfyHelper.java`
    8. Make it implement `ServletContentListener`
    9. In the contextInitialized method call `ObjectifyService.register` twice, once for each business class, passing the class as a parameter. e.g. `ObjectifyService.register(Guestbook.class);`
    10. Throw an `UnsupportedOperationException` in contextDestroyed method.
7. Create a new servlet: `SignGuestbookServlet`, implement the `doPost` method as follows:
	9. Create a local `Greeting greeting;`
	10. Create a `UserService` and get the current `User`
	11. Get the parameters `guestbookName` and `content`
	12. Create a greeting from the parameters and, if not null, the current user.
	13. Save the greeting to the database:
        	
        ```java
        // Use Objectify to save the greeting and now() is used to make the call synchronously as we  
        // will immediately get a new page using redirect and we want the data to be present.  
        ObjectifyService.ofy().save().entity(greeting).now();
        ```
        
    14. Redirect the browser to the guestbook.jsp:
    
        ```java
        response.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
        ```
        
7. Update our JSP `guestbook.jsp` to read from the datastore:
    9. Following the greeting create a database key from the guestbookName.
	
        ```java
        Key<Guestbook> theBook = Key.create(Guestbook.class, guestbookName);
        ```
        
    9. Retrieve up to 5 entries from the guest book in date order.
	
          ```java
          List<Greeting> greetings = ObjectifyService.ofy()  
                  .load()  
                  .type(Greeting.class) // We want only Greetings  
                  .ancestor(theBook)    // Anyone in this book  
                  .order("-date")       // Most recent first - date is indexed.  
                  .limit(5)             // Only show 5 of them.  
                  .list();
          ```
          
    10. Output the retrieved greetings:
        ```jsp
        if (greetings.isEmpty()) {
        %>
        <p>Guestbook '${fn:escapeXml(guestbookName)}' has no messages.</p>
        <%
            } else {
        %>
        <p>Messages in Guestbook '${fn:escapeXml(guestbookName)}'.</p>
        <%
              // Look at all of our greetings
                for (Greeting greeting : greetings) {
                    pageContext.setAttribute("greeting_content", greeting.content);
                    String author;
                    if (greeting.author_email == null) {
                        author = "An anonymous person";
                    } else {
                        author = greeting.author_email;
                        String author_id = greeting.author_id;
                        if (user != null && user.getUserId().equals(author_id)) {
                            author += " (You)";
                        }
                    }
                    pageContext.setAttribute("greeting_user", author);
        %>
        <p><b>${fn:escapeXml(greeting_user)}</b> wrote:</p>
        <blockquote>${fn:escapeXml(greeting_content)}</blockquote>
        <%
                }
            }
        %>
        ```
        
    11. Create a form to add a new guestbook entry.
    
        ```html
        <form action="/sign" method="post">
            <div><textarea name="content" rows="3" cols="60"></textarea></div>
            <div><input type="submit" value="Post Greeting"/></div>
            <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
        </form>
        <%-- //[END datastore]--%>
        <form action="/guestbook.jsp" method="get">
            <div><input type="text" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/></div>
            <div><input type="submit" value="Switch Guestbook"/></div>
        </form>
        ```
        
8. Set up our database in `web.xml`

    ```xml
    <filter>
        <filter-name>ObjectifyFilter</filter-name>
        <filter-class>com.googlecode.objectify.ObjectifyFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>ObjectifyFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    <listener>
        <listener-class>com.example.guestbook.OfyHelper</listener-class>
    </listener>
    ```
    
9. This completes the turtorial for Google App Engine. Run the program and attempt to use the guestbook.
 
This should give you enough understanding to get started with building web applications. You will need to learn more about databases and the user service to move forward. There are excellent online training resources.

If you want to start a new Google App Engine project in IDEA the use New project > maven and build the project from one of the maven archetypes found http://search.maven.org/#search%7Cga%7C1%7Ccom.google.appengine.archetypes

