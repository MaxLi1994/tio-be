package validators;

/**
 * @author Jieying Xu
 */
public class IntegerFormatValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String idStr = (String)input;
        try {
            int id = Integer.parseInt(idStr);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getErrorMsg(String inputName) {
        return inputName + " should be legal int format!";
    }
}
