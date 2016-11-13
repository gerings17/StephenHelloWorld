import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by league on 11/13/16.
 */
@Entity
public class Guestbook {
    @Id public String book;
}
