package models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class TpUser extends Model<TpUser> {
    public static final TpUser dao = new TpUser().dao();
}
