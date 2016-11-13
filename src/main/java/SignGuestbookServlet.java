import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.StrictErrorHandler;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.common.base.Flag;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by league on 11/13/16.
 */
public class SignGuestbookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Greeting greeting;
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String guestbookName = request.getParameter("guestbookName");
        String content = request.getParameter("content");
        if (user == null) {
            greeting = new Greeting(guestbookName, content);
        } else {
            greeting = new Greeting(guestbookName, content, user.getEmail());
        }
        // Use Objectify to save the greeting and now() is used to make the call synchronously as we
        // will immediately get a new page using redirect and we want the data to be present.
        ObjectifyService.ofy().save().entity(greeting).now();
        response.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
