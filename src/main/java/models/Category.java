package models;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Jieying Xu
 */
public class Category extends Model<Category> {
    public static final Category dao = new Category().dao();
}
