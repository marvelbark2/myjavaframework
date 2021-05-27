package ws.prospeak.myweb.framework.Illuminate.routing;

public enum HttpMethod {
    INSTANCE, GET, POST, PUT, PATCH, DELETE;

    public HttpMethod findMethod(String method) {
        switch (method) {
            case "GET":
                return GET;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "PATCH":
                return PATCH;
            case "DELETE":
                return DELETE;
            default:
                return null;
        }
    }
}
