package Models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class User extends Model<User> {
    public static final User dao = new User().dao();
}
