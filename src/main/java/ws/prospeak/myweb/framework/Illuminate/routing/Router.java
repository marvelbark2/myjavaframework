package ws.prospeak.myweb.framework.Illuminate.routing;

import org.springframework.util.AntPathMatcher;

import java.lang.reflect.Method;
import java.util.Map;

public class Router {
    private final AntPathMatcher matcher = new AntPathMatcher();

    private final String baseRoute;
    private HttpMethod method;
    private Object callback;
    private boolean containParams;

    public Router(String baseRoute, HttpMethod method, Object callback) {
        this.baseRoute = baseRoute;
        this.method = method;
        this.callback = callback;
        this.containParams = baseRoute.contains("}");
    }

    public String getBaseRoute() {
        return baseRoute;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Object getCallback() {
        return callback;
    }

    public boolean isContainParams() {
        return containParams;
    }

    public boolean isValidUrl(String url) {
        return  matcher.match(this.baseRoute, url);
    }

    public Object[] values(String url) {
            Map<String, String> values = matcher.extractUriTemplateVariables(this.baseRoute, url);
            return values.values().toArray();
    }

    @Override
    public String toString() {
        return "Router{" +
                "baseRoute='" + baseRoute + '\'' +
                ", method=" + method +
                ", callback=" + callback +
                ", containParams=" + containParams +
                '}';
    }
}
