package validators;

import models.Category;

/**
 * @author Jieying Xu
 */
public class CategoryRecordExistValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String categoryName = (String)input;
        return Category.dao.find("select * from category where name = ?", categoryName).size() != 0;
    }

    @Override
    public String getErrorMsg(String inputName) {
        return "Category not found!";
    }
}
