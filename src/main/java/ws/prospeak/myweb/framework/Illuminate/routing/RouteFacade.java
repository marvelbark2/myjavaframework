package ws.prospeak.myweb.framework.Illuminate.routing;

import java.util.List;

public class RouteFacade {

    public static void get(String route, CallBack callBack) {
        RouteService.INSTANCE.get(route, callBack);
    }

    public static void get(String route, Class clazz, String callback) {
        RouteService.INSTANCE.get(route, clazz, callback);
    }
    public static void post(String route, Class clazz, String callback) {
        RouteService.INSTANCE.post(route,clazz,callback);
    }

    public static void delete(String route, Class clazz, String callback) {
        RouteService.INSTANCE.delete(route,clazz,callback);
    }

    public static void patch(String route, Class clazz, String callback) {
        RouteService.INSTANCE.patch(route,clazz,callback);
    }

    public static void put(String route, Class clazz, String callback) {
        RouteService.INSTANCE.put(route,clazz,callback);
    }

    public static List<Router> getRouters() {
        return RouteService.INSTANCE.getRouters();
    }
}
