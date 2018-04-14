package annotations;

import java.lang.reflect.Method;

/**
 * @author Jieying Xu
 */
public class ValidateAnnotationHandler {
    public static String annotationHandler(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method: methods) {
            ValidatePara[] requests = method.getDeclaredAnnotationsByType(ValidatePara.class);
            for(ValidatePara request: requests) {
                String paraStr = request.value();
                if (paraStr == null) {
                    return "Null";
                } else if (paraStr.trim().equals("")) {
                    return "Empty";
                } else {
                    return "valid";
                }
            }
        }
        return "No annotations";
    }
}
