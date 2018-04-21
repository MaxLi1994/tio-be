package validators;

/**
 * @author Jieying Xu
 */
public class TpTypeValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        String type = (String)input;
        return type.equals("google");
    }

    @Override
    public String getErrorMsg(String inputName) {
        return inputName + " is not a legal type!";
    }
}
