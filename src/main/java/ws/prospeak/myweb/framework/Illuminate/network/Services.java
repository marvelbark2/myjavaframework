package ws.prospeak.myweb.framework.Illuminate.network;

import ws.prospeak.myweb.framework.Illuminate.network.rmi.RmiService;
import ws.prospeak.myweb.framework.Illuminate.network.sockets.SocketService;

import java.rmi.registry.Registry;

public enum Services {
    INSTANCE;
    private final RmiService rmi;
    private final SocketService socket;
    Services() {
        rmi = new RmiService();
        socket = new SocketService();
    }

    public Registry rmi() {
        rmi.enableRmi();
        return rmi.getRegistry();
    }

    public SocketService socket() {
        return socket;
    }
}
