package ws.prospeak.myweb.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import ws.prospeak.myweb.framework.Illuminate.routing.CallBack;
import ws.prospeak.myweb.framework.Illuminate.routing.HttpMethod;
import ws.prospeak.myweb.framework.Illuminate.routing.RouteFacade;
import ws.prospeak.myweb.framework.Illuminate.routing.Router;
import ws.prospeak.myweb.framework.app.models.ValueExampleObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            System.exit(-1);
        }
    }

    @Override
    public void handle(HttpExchange exchange) {
        Router router = null;
        Object params = null;
        String path = exchange.getRequestURI().getPath();
        if(! path.equals("/")) {
            List<String> routeList = Arrays.stream(path.split("/")).collect(Collectors.toList());
            if(routeList.size() > 0) {
                routeList.remove(0);
                routeList.add(0, "/"+ routeList.remove(0));
            }
            LinkedList<String> routeLinkedList = new LinkedList<>(routeList);
            String pathBase = routeLinkedList.getFirst();
            for (Router route: routes) {
                String routeBase = route.getBaseRoute();
                if(route.getMethod().equals(HttpMethod.GET)) {
                    if(pathBase.equals(routeBase)) {
                        if(routeLinkedList.size() == 1 && route.getParam() == null)
                            router = route;
                        else if(routeLinkedList.size() > 1 && route.getParam() != null) {
                            StringUtils.difference("/about/:id/cast", "/about/25/cast");
                            params = routeLinkedList.getLast();
                            router = route;
                        }
                    }

                }
                else {
                    System.out.println(path + " is started with " + routeLinkedList.getFirst());
                }
            }
        } else {
            router = routes.stream().filter(router1 -> router1.getBaseRoute().equals("/")).findFirst().orElse(null);
        }

        System.out.println(router);
        if(router != null) {
           if(router.getParam() == null) {
               try {
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
                   try {
                       handleResponse(exchange, requestParamValue);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                       exchange.close();
                   }

               } catch (Exception e) {
                   e.printStackTrace();
                   exchange.close();
               }
           } else {
               try {
                   Object requestParamValue = null;
                   Object callback = router.getCallback();
                   if(callback instanceof Method) {
                       Method response = (Method) callback;
                       Class.forName(response.getDeclaringClass().getName());
                       requestParamValue = response.invoke(response.getDeclaringClass().newInstance(), params);
                   } else if(callback instanceof CallBack)
                       requestParamValue = ((CallBack) callback).callBack(params);
                   try {
                       handleResponse(exchange, requestParamValue);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
                   exchange.close();
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
