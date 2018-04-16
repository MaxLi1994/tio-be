package interceptors;

import annotations.ValidatePara;
import com.jfinal.aop.Invocation;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jieying Xu
 */
public class ParaValidateInterceptor implements Interceptor{
    @Override
    public void intercept(Invocation invocation) {
        Controller c = invocation.getController();
        ValidatePara[] vpList = invocation.getMethod().getDeclaredAnnotationsByType(ValidatePara.class);
        for(ValidatePara vp: vpList) {
            String paraName = vp.value();
            String paraValue = c.getPara(paraName);
            Class[] clazzList = vp.validators();
            for(Class clazz: clazzList) {
                try {
                    Method validateMt = clazz.getDeclaredMethod("validate", Object.class);
                    Object flag = validateMt.invoke(clazz.newInstance(), paraValue);
                    if (!(boolean)flag) {
                        Method msgMt = clazz.getDeclaredMethod("getErrorMsg", String.class);
                        String errMsg = (String)(msgMt.invoke(clazz.newInstance(), paraName));
                        Map<String, Object> result = new HashMap<>();
                        result.put("code", -1);
                        result.put("msg", errMsg);
                        c.renderJson(result);
                        return;
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    c.renderJson("enter catch block");
                    e.printStackTrace();
                    return;
                }
            }
        }
        invocation.invoke();
    }
}
