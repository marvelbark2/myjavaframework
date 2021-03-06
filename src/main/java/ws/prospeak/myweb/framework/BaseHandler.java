package ws.prospeak.myweb.framework;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.prospeak.myweb.framework.Illuminate.routing.*;

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

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            Router router = routes
                    .stream()
                    .filter(route -> route.isValidUrl(path) && Objects.equals(HttpMethod.INSTANCE.findMethod(exchange.getRequestMethod()), route.getMethod()))
                    .findAny().orElse(null);

            logger.info(String.valueOf(router));
            System.out.println(exchange.getRequestMethod());
            if(router != null) {
                if(router.getMethod().equals(HttpMethod.GET)) {
                    handleGet(exchange, router);
                }
                else if(router.getMethod().equals(HttpMethod.POST)) {
                    handlePost(exchange, router);
                }
            } else {
                notFound(exchange);
            }
        } catch (Exception exception) {
            handleError(exchange, exception);
        } finally {
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange, Router router) throws Exception {
        String path = exchange.getRequestURI().getPath();
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
    }
    private void handlePost(HttpExchange exchange, Router router) throws Exception {
        String path = exchange.getRequestURI().getPath();
        Request request = new Request(exchange);
        if(!router.isContainParams()) {
            Object callback = router.getCallback();
            Object requestParamValue = null;
            if(callback instanceof Method) {
                Method response = (Method) callback;
                Class.forName(response.getDeclaringClass().getName());
                requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), request);
            } else if(callback instanceof PostCallBack) {
                PostCallBack callBack = (PostCallBack) callback;
                requestParamValue = callBack.callBack(request);
            }
            handleResponse(exchange, requestParamValue);
        } else {
            Object requestParamValue = null;
            Object[] params = router.values(path);
            Object callback = router.getCallback();
            if(callback instanceof Method) {
                Method response = (Method) callback;
                Class.forName(response.getDeclaringClass().getName());
                requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), request, params);
            } else if(callback instanceof PostCallBack)
                requestParamValue = ((PostCallBack) callback).callBack(request, params);
            handleResponse(exchange, requestParamValue);
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

    @SneakyThrows
    private void handleError(HttpExchange httpExchange, Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        String response = stringWriter.toString();
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(500, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
