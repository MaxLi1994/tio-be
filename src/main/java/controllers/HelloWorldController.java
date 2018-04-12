package controllers;

import com.jfinal.core.Controller;

/**
 * @author Jieying Xu
 */
public class HelloWorldController extends Controller {

    public void index() {
        renderText("Hello Maven JFinal!");
    }

    public void test() {
        redirect("http://google.com");
    }
}