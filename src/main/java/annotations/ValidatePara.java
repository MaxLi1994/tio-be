package annotations;
import java.lang.annotation.*;

/**
 * @author Jieying Xu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ValidateParas.class)
public @interface ValidatePara {
    String value();
    Class[] validators();
}
