package ws.prospeak.myweb.framework;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ApplicationBoot {
    final static Logger logger = LoggerFactory.getLogger(ApplicationBoot.class.getName());
    public static void main(String[] args) {
        logger.info("Application started");
        ThreadExample service = new ThreadExample();
        service.start();
    }
    static class ThreadExample extends Thread {
        @Override
        public void run() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
                server.createContext("/", new BaseHandler());
                server.setExecutor(null);
                logger.info("Http service is starting");
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
