import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.Date;

/**
 * Created by league on 11/13/16.
 */
@Entity
public class Greeting {
    @Parent
    Key<Guestbook> theBook;
    @Id
    public Long id;
    public String author_email;
    public String author_id;
    public String content;
    @Index
    public Date date;

    public Greeting() {
        this.date = new Date();
    }

    public Greeting(String book, String content) {
        this.content = content;
        if (book != null) {
            theBook = Key.create(Guestbook.class, book);  // Creating the Ancestor key
        } else {
            theBook = Key.create(Guestbook.class, "default");
        }
    }

    public Greeting(String book, String content, String author_email) {
        this(book, content);
        this.author_email = author_email;
    }
}
