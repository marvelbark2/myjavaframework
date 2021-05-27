package ws.prospeak.myweb.framework.Illuminate.routing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public enum RouteService {
    INSTANCE;
    private List<Router> routers = new ArrayList<>();

    public void get(String route, CallBack callBack) {
        try {
            routers.add(new Router(route, HttpMethod.GET, callBack));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void get(String route, Class clazz, String callback) {
        try {
            if(route.contains("{")) {
                Method method = clazz.getDeclaredMethod(callback, Object.class);
                routers.add(new Router(route, HttpMethod.GET, method));
            } else {
                Method method = clazz.getDeclaredMethod(callback, null);
                routers.add(new Router(route, HttpMethod.GET, method));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void post(String route, Class clazz, String callback) {
        try {
            if(route.contains("}")) {
                Method method = clazz.getDeclaredMethod(callback, Object.class);
                routers.add(new Router(route, HttpMethod.POST, method));
            } else {
                Method method = clazz.getDeclaredMethod(callback, null);
                routers.add(new Router(route, HttpMethod.POST, method));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void delete(String route, Class clazz, String callback) {
        try {
            boolean param = route.contains("}");
            Method method = clazz.getDeclaredMethod(callback, !param ? null: Object.class);
            routers.add(new Router(route, HttpMethod.DELETE, method));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void patch(String route, Class clazz, String callback) {
        try {
            boolean param = route.contains("}");
            Method method = clazz.getDeclaredMethod(callback,  !param ? null: Object.class);
            routers.add(new Router(route, HttpMethod.PATCH, method));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void put(String route, Class clazz, String callback) {
        try {
            boolean param = route.contains("}");
            Method method = clazz.getDeclaredMethod(callback, !param ? null: Object.class);
            routers.add(new Router(route, HttpMethod.PUT, method));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Router> getRouters() {
        return routers;
    }

}
