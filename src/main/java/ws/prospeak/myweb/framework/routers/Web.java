package ws.prospeak.myweb.framework.routers;

import ws.prospeak.myweb.framework.Illuminate.RouteFacade;
import ws.prospeak.myweb.framework.app.controllers.Controller;

public enum Web {
    INSTANCE;

    Web() {
        RouteFacade.get("/", Controller.class, "index");
        RouteFacade.get("/hello", Controller.class, "test");
        RouteFacade.get("/about", Controller.class, "about");
        RouteFacade.get("/about/:id", Controller.class, "params");
    }
}
