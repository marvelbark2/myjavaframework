package ws.prospeak.myweb.framework.Illuminate.routing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import  com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

public class Request {
    private final ObjectMapper mapper = new ObjectMapper();

    private final Headers header;
    private final InetSocketAddress address;
    private  JsonNode body;
    private String fullPath;
    private String route;
    private String userInfo;

    public Request(final HttpExchange exchange) {
        header = exchange.getRequestHeaders();
        address = exchange.getRemoteAddress();
        route = exchange.getRequestURI().getPath();
        fullPath = "http://"+exchange.getRequestHeaders().getFirst("Host") + exchange.getRequestURI();
        userInfo = exchange.getRequestURI().getUserInfo();
        try {
            body = mapper.readTree(exchange.getRequestBody());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public JsonNode get(String key) {
        return body.get(key);
    }

    public Object header(String key) {
        return header.get(key);
    }

    public Boolean hasHeader(String key) {
        return header.containsKey(key);
    }

    public JsonNode all() {
        return body;
    }

    public String requestIp() {
        return address.getAddress().getHostAddress();
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getRoute() {
        return route;
    }

    public String getUserInfo() {
        return userInfo;
    }
    public Object headers() {
        return header;
    }
}
