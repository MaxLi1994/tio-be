package validators;

/**
 * @author Jieying Xu
 */
public abstract class AbstractValidator {
    public abstract boolean validate(Object input);
    public abstract String getErrorMsg(String inputName);
}
