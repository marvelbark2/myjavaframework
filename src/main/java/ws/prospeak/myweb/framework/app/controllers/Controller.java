package ws.prospeak.myweb.framework.app.controllers;


public class Controller {

    public String test() {
        return "Hello page";
    }
    public String index() {
        return "Home Page";
    }
    public String about() {
        return "Welcome in About page";
    }
    public String params(Object id) {
        return "This is id " + id;
    }
}
