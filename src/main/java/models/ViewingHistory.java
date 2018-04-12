package models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class ViewingHistory extends Model<ViewingHistory> {
    public static final ViewingHistory dao = new ViewingHistory().dao();
}
