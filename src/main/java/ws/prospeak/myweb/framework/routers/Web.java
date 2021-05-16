package ws.prospeak.myweb.framework.routers;

import ws.prospeak.myweb.framework.Illuminate.routing.CallBack;
import ws.prospeak.myweb.framework.Illuminate.routing.RouteFacade;
import ws.prospeak.myweb.framework.app.controllers.Controller;

public enum Web {
    INSTANCE;

    Web() {
        RouteFacade.get("/", Controller.class, "index");
        RouteFacade.get("/hello", Controller.class, "test");
        RouteFacade.get("/about", Controller.class, "about");
        RouteFacade.get("/about/:id", Controller.class, "params");
        RouteFacade.get("/testMethod", new CallBack() {
            @Override
            public Object callBack() {
                return "testing";
            }
        });
        RouteFacade.get("/testMethod/:id", new CallBack() {
            @Override
            public Object callBack(Object... t) {
                return "Getting " + t[0];
            }
        });
    }
}
