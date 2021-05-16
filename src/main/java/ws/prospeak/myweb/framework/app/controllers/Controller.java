package ws.prospeak.myweb.framework.app.controllers;


import ws.prospeak.myweb.framework.Illuminate.view.View;

import java.util.Map;

public class Controller {

    public String test() {
        return View.json(Map.of("success", true));
    }
    public String index() {
        return View.render("test.egde");
    }
    public String about() {
        return "Welcome in About page";
    }
    public String params(Object id) {
        return "This is id " + id;
    }
}
