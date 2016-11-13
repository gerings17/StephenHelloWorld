import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by league on 11/13/16.
 */
public class OfyHelper implements ServletContextListener{
    public void contextInitialized(ServletContextEvent servletContextEvent){
        ObjectifyService.register(Greeting.class);
        ObjectifyService.register(Guestbook.class);
    }
    public void contextDestroyed(ServletContextEvent servletContextEvent)throws UnsupportedOperationException{

    }

}
