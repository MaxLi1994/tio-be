package interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import config.Const;

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
                break;
            case Const.CODE_SUCCESS:
                result.put("code", code);
                result.put("data", c.getAttr("data"));
                break;
        }
        c.renderJson(result);
    }
}
