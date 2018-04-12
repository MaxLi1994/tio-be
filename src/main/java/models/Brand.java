package models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class Brand extends Model<Brand> {
    public static final Brand dao = new Brand().dao();
}
