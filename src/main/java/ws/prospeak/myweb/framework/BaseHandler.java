package ws.prospeak.myweb.framework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.prospeak.myweb.framework.Illuminate.routing.CallBack;
import ws.prospeak.myweb.framework.Illuminate.routing.HttpMethod;
import ws.prospeak.myweb.framework.Illuminate.routing.RouteFacade;
import ws.prospeak.myweb.framework.Illuminate.routing.Router;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;


public class BaseHandler implements HttpHandler {
    private List<Router> routes = RouteFacade.getRouters();
    private final  Logger logger = LoggerFactory.getLogger(BaseHandler.class.getName());

    public BaseHandler() {
        try {
            Class.forName("ws.prospeak.myweb.framework.routes.Web");
            routes = RouteFacade.getRouters();
            System.out.println(routes);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        Router router = routes
                .stream()
                .filter(route -> route.isValidUrl(path) && Objects.equals(HttpMethod.INSTANCE.findMethod(exchange.getRequestMethod()), route.getMethod()))
                .findAny().orElse(null);

        System.out.println(router);
        System.out.println(exchange.getRequestMethod());
        if(router != null) {
            if(!router.isContainParams()) {
                Object callback = router.getCallback();
                Object requestParamValue = null;
                if(callback instanceof Method) {
                    Method response = (Method) callback;
                    Class.forName(response.getDeclaringClass().getName());
                    requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), null);
                } else if(callback instanceof CallBack) {
                    CallBack callBack = (CallBack) callback;
                    requestParamValue = callBack.callBack();
                }
                handleResponse(exchange, requestParamValue);
            } else {
                Object requestParamValue = null;
                Object[] params = router.values(path);
                Object callback = router.getCallback();
                if(callback instanceof Method) {
                    Method response = (Method) callback;
                    Class.forName(response.getDeclaringClass().getName());
                    requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), params);
                } else if(callback instanceof CallBack)
                    requestParamValue = ((CallBack) callback).callBack(params);
                handleResponse(exchange, requestParamValue);
            }
        } else {
            notFound(exchange);
        }
    }
    private void handleResponse(HttpExchange httpExchange, Object callback) throws Exception {
        OutputStream outputStream = httpExchange.getResponseBody();
        String htmlResponse = callback.toString();
        logger.info("Response is {}", htmlResponse);
        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        PrintWriter writer = new PrintWriter(outputStream);
        writer.print(htmlResponse);
        writer.flush();
        writer.close();
    }
    private void notFound(HttpExchange httpExchange) {
        OutputStream outputStream = httpExchange.getResponseBody();
        try {

            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setClassForTemplateLoading(BaseHandler.class, "/views/");
            Template template = cfg.getTemplate("404.egde");
            StringWriter stringWriter = new StringWriter();
            template.process(null, stringWriter);
            String htmlResponse = stringWriter.toString();

            // this line is a must
            httpExchange.sendResponseHeaders(404, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
