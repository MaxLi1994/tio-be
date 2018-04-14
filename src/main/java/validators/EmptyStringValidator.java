package validators;

/**
 * @author Jieying Xu
 */
public class EmptyStringValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String s = (String)input;
        return !s.trim().equals("");
    }

    @Override
    public String getErrorMsg(String inputName) {
        return inputName + " is empty string!";
    }

}
