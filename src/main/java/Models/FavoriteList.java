package Models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class FavoriteList extends Model<FavoriteList> {
    public static final FavoriteList dao = new FavoriteList().dao();
}
