import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by BigRockerEnding on 10/30/16.
 */
public class HelloWorldServlet extends HttpServlet {
    static int counter = 0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getQueryString();
        int countStart;
        if (query == null || query.isEmpty()) {
            countStart = 0;
        } else {
            String[] params = query.split("=");
            countStart = Integer.parseInt(params[1]);
        }
        PrintWriter outWriter = response.getWriter();
        //outWriter.println("\"Hello World!\" says Stephen. \"");
        //outWriter.format("Why have I had to do this %d + %d times? That's %d times!",
        //        countStart, ++counter, countStart + counter);
        outWriter.println("<html>");
        outWriter.println("<head><title>Hello World</title></head>");
        outWriter.println("<body>");
        outWriter.println("<h1>Hello World!</h1>");
        outWriter.println("<p><a href=\"http://haneke.net\">This</a> is an awesome site!</p>");
        outWriter.println("</body>");
        outWriter.println("</html>");
    }
}
