package ws.prospeak.myweb.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import ws.prospeak.myweb.framework.Illuminate.HttpMethod;
import ws.prospeak.myweb.framework.Illuminate.RouteFacade;
import ws.prospeak.myweb.framework.Illuminate.Router;
import ws.prospeak.myweb.framework.app.models.ValueExampleObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class BaseHandler implements HttpHandler {
    private List<Router> routes;
    private ObjectMapper mapper = new ObjectMapper();

    public BaseHandler() {
        try {
            Class.forName("ws.prospeak.myweb.framework.routers.Web");
            routes = RouteFacade.getRouters();
            System.out.println(routes);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange exchange) {
        Router router = null;
        Object params = null;
        String path = exchange.getRequestURI().getPath();
        for (Router route: routes) {
            if(route.getBaseRoute().equals(path) && route.getMethod().equals(HttpMethod.GET)) {
                router = route;
            }
            else if(path.startsWith(router.getBaseRoute()) && router.getParam().equals("id")) {
//                router = route;
//                String[] split = path.split("/");
//                params = split[split.length - 1];
            }
        }
        System.out.println(router);
        if(router != null) {
           if(router.getParam() == null) {
               try {
                   Method response = router.getCallback();
                   Class.forName(response.getDeclaringClass().getName());
                   Object requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), null);
                   try {
                       handleResponse(exchange, requestParamValue);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           } else {
               try {
                   Method response = router.getCallback();
                   Class.forName(response.getDeclaringClass().getName());
                   Object requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), params);
                   try {
                       handleResponse(exchange, requestParamValue);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        } else {
            notFound(exchange);
        }

    }
    private void handleResponse(HttpExchange httpExchange, Object callback) throws Exception {
        OutputStream outputStream = httpExchange.getResponseBody();

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(BaseHandler.class, "/views/");

        Map<String, Object> input = new HashMap<>();
        input.put("title", callback.toString());

        input.put("exampleObject", new ValueExampleObject("Java object", "me"));

        List<ValueExampleObject> systems = new ArrayList<>();
        systems.add(new ValueExampleObject("Android", "Google"));
        systems.add(new ValueExampleObject("iOS States", "Apple"));
        systems.add(new ValueExampleObject("Ubuntu", "Canonical"));
        systems.add(new ValueExampleObject("Windows7", "Microsoft"));
        input.put("systems", systems);

        Template template = cfg.getTemplate("test.egde");

        StringWriter stringWriter = new StringWriter();

        template.process(input, stringWriter);
        String htmlResponse = stringWriter.toString();

        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
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
