package ws.prospeak.myweb.framework.routes;

import ws.prospeak.myweb.framework.Illuminate.routing.CallBack;
import ws.prospeak.myweb.framework.Illuminate.routing.RouteFacade;
import ws.prospeak.myweb.framework.app.controllers.Controller;
import ws.prospeak.myweb.framework.app.models.Users;

public enum Web {
    INSTANCE;

    Web() {
        RouteFacade.get("/", Controller.class, "index");
        RouteFacade.get("/hello", Controller.class, "test");
        RouteFacade.get("/about", Controller.class, "about");
        RouteFacade.get("/about/{id}", Controller.class, "params");
        RouteFacade.get("/testMethod", new CallBack() {
            @Override
            public Object callBack() {
                return "testing";
            }
        });
        RouteFacade.get("/testMethod/{id}", new CallBack() {
            @Override
            public Object callBack(Object... t) {
                return "Getting " + t[0];
            }
        });
        RouteFacade.get("/testMethod/{id}/{id2}", new CallBack() {
            @Override
            public Object callBack(Object[] t) {
                return "first id is " + t[0] + " and the second is " + t[1];
            }
        });
        RouteFacade.get("/users", new CallBack() {
            @Override
            public Object callBack() {
                try {
                    Users users = new Users();
                    return users.all();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        });
        RouteFacade.get("/users/{id}", new CallBack() {
            @Override
            public Object callBack(Object... t) {
                try {
                    Users users = new Users();
                    return users.find(t[0]);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        });
    }
}
