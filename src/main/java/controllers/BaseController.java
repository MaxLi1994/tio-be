package controllers;

import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.Map;

import static config.Const.*;

public class BaseController extends Controller {
    private Map<String, Object> responseObj = new HashMap<>();



    public void successResponse(Object response) {
        setAttr("data", response);
        setAttr("code", CODE_SUCCESS);
    }

    public void successResponse(String key, Object value) {
        responseObj.put(key, value);
        setAttr("data", responseObj);
        setAttr("code", CODE_SUCCESS);
    }

    public void errorResponse(String message) {
        setAttr("msg", message);
        setAttr("code", CODE_ERROR);
    }

}
