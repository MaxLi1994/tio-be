package validators;

/**
 * @author Jieying Xu
 */
public class NullValidator extends AbstractValidator {
    @Override
    public boolean validate(Object input) {
        return input != null;
    }

    @Override
    public String getErrorMsg(String inputName) {
        return inputName + " is null!";
    }
}
