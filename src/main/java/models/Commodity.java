package models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class Commodity extends Model<Commodity> {
    public static final Commodity dao = new Commodity().dao();
}
