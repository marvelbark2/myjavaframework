package ws.prospeak.myweb.framework.Illuminate.routing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public enum RouteService {
    INSTANCE;
    private List<Router> routers = new ArrayList<>();

    public void get(String route, CallBack callBack) {
        try {
            String[] path = route.split("/:");
            if(path.length >= 2) {
                String param = path[1];
                routers.add(new Router(path[0], param, HttpMethod.GET, callBack));
            } else {
                String param = null;
                routers.add(new Router(path[0], param, HttpMethod.GET, callBack));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void get(String route, Class clazz, String callback) {
        try {
            String[] path = route.split("/:");
            if(path.length >= 2) {
                String param = path[1];
                Method method = clazz.getDeclaredMethod(callback, Object.class);
                routers.add(new Router(path[0], param, HttpMethod.GET, method));
            } else {
                String param = null;
                Method method = clazz.getDeclaredMethod(callback, null);
                routers.add(new Router(path[0], param, HttpMethod.GET, method));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void post(String route, Class clazz, String callback) {
        try {
            String[] path = route.split("/:");
            String param = path.length > 1 ? path[1] : null;
            Method method = clazz.getDeclaredMethod(callback, param == null ? null: Object.class);
            routers.add(new Router(path[0], param, HttpMethod.POST, method));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void delete(String route, Class clazz, String callback) {
        try {
            String[] path = route.split("/:");
            String param = path.length > 1 ? path[1] : null;
            Method method = clazz.getDeclaredMethod(callback, param == null ? null: Object.class);
            routers.add(new Router(path[0], param, HttpMethod.DELETE, method));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void patch(String route, Class clazz, String callback) {
        try {
            String[] path = route.split("/:");
            String param = path.length > 1 ? path[1] : null;
            Method method = clazz.getDeclaredMethod(callback, param == null ? null: Object.class);
            routers.add(new Router(path[0], param, HttpMethod.PATCH, method));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void put(String route, Class clazz, String callback) {
        try {
            String[] path = route.split("/:");
            String param = path.length > 1 ? path[1] : null;
            Method method = clazz.getDeclaredMethod(callback, param == null ? null: Object.class);
            routers.add(new Router(path[0], param, HttpMethod.PUT, method));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Router> getRouters() {
        return routers;
    }

}
