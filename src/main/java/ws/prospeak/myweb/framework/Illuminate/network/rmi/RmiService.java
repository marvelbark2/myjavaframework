package ws.prospeak.myweb.framework.Illuminate.network.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class RmiService {
    private final int PORT;
    private Registry registry;

    public RmiService() {
        PORT = 1099;
        System.out.println(PORT);
    }

    public void enableRmi() {
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Registry getRegistry() {
        return registry;
    }
}
