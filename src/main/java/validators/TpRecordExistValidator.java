package validators;

import models.TpUser;

/**
 * @author Jieying Xu
 */
public class TpRecordExistValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String tpId = (String)input;
        TpUser myUser = TpUser.dao.findFirst("select * from tp_user where tp_id = ?", tpId);
        return myUser != null;
    }

    @Override
    public String getErrorMsg(String inputName) {
        return "Third-party user not found!";
    }
}
