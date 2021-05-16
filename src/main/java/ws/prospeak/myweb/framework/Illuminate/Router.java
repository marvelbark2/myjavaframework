package ws.prospeak.myweb.framework.Illuminate;

import java.lang.reflect.Method;

public class Router {
    private String baseRoute;
    private String param;
    private HttpMethod method;
    private Method callback;

    public Router(String baseRoute, String param, HttpMethod method, Method callback) {
        this.baseRoute = baseRoute;
        this.param = param;
        this.method = method;
        this.callback = callback;
    }

    public String getBaseRoute() {
        return baseRoute;
    }

    public String getParam() {
        return param;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Method getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return "Router{" +
                "baseRoute='" + baseRoute + '\'' +
                ", param='" + param + '\'' +
                ", method=" + method +
                ", callback=" + callback +
                '}';
    }
}
