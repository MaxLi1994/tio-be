package models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class ViewingHistory extends Model<ViewingHistory> implements Comparable<ViewingHistory>{
    public static final ViewingHistory dao = new ViewingHistory().dao();

    @Override
    public int compareTo(ViewingHistory o) {
        return Integer.compare(o.getInt("id"), this.getInt("id"));
    }
}
