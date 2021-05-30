package ws.prospeak.myweb.framework.Illuminate.network.sockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ws.prospeak.myweb.framework.Illuminate.collection.CollectionEntity;
import ws.prospeak.myweb.framework.Illuminate.database.orm.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketService {
    private final int PORT = 60254;
    private ServerSocket server;
    private final ObjectMapper mapper = new ObjectMapper();

    public SocketService() {
        try {
            server = new ServerSocket(PORT);
            mapper.registerModule(new JavaTimeModule());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public <T extends Models>  void enableTcp(T model) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        while (true){
            Socket socket = server.accept();
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String request;
            while ((request = reader.readLine()) != null) {
                Request requestObject = mapper.readValue(request, Request.class);
                Response response = new Response();
                String event = requestObject.getEvent();
                JsonNode data = requestObject.getData();

                List<Method> methods = List.of(model.getClass().getMethods());
                Method method = methods.stream().filter(mt -> mt.getName().equals(event)).findAny().orElse(null);

                if(method != null) {
                    Object message;
                    if(data.size() == 0)
                        message = method.invoke(model, null);
                    else {
                        Object[] dataObj = List.of(data).toArray();
                        message = method.invoke(model, dataObj);
                    }
                    response.setEvent(event);
                    response.setSuccess(true);
                    response.setMessage(message);
                    if(message instanceof CollectionEntity)
                        response.setDataType("Collection-" + model.getClass().getSimpleName());
                    else if(message instanceof Models) {
                        response.setDataType("Model-" + model.getClass().getSimpleName());
                    } else {
                        response.setDataType("Object-" + model.getClass().getSimpleName());
                    }
                } else {
                    response.setEvent(event);
                    response.setSuccess(false);
                    response.setMessage("No method found");
                }
                writer.println(mapper.writeValueAsString(response));
            }
        }
    }
}
