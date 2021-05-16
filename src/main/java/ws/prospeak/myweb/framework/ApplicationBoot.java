package ws.prospeak.myweb.framework;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;

public class ApplicationBoot {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/", new BaseHandler());
        server.setExecutor(null);
        server.start();

    }
}
