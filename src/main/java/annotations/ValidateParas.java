package annotations;

import java.lang.annotation.*;

/**
 * @author Jieying Xu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateParas {
    ValidatePara[] value();
}
