package interceptors;

import annotations.ValidatePara;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import config.Const;
import validators.AbstractValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ResponseInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        invocation.invoke();

        Controller c = invocation.getController();
        int code = c.getAttrForInt("code");

        Map<String, Object> result = new HashMap<>();
        switch (code) {
            case Const.CODE_ERROR:
                result.put("code", code);
                result.put("msg", c.getAttr("msg"));
                System.out.println(result);
                break;
            case Const.CODE_SUCCESS:
                result.put("code", code);
                result.put("data", c.getAttr("data"));
                break;
        }
        c.renderJson(result);
    }
}
