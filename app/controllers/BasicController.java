package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class BasicController extends Controller {

    public Result index() {
        return ok("Hello World Another");
    }
}
