package ws.prospeak.myweb.framework.Illuminate.network;

import ws.prospeak.myweb.framework.Illuminate.database.orm.Model;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClientExample {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry();
        Model server = (Model) registry.lookup("users");
        System.out.println(server.all());
    }
}
