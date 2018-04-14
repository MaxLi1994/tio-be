package validators;

import models.User;

/**
 * @author Jieying Xu
 */
public class UserRecordExistValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String id = (String)input;
        User myUser = User.dao.findById(Integer.parseInt(id));
        return myUser != null;
    }

    @Override
    public String getErrorMsg(String inputName) {
        return "User not found!";
    }
}
